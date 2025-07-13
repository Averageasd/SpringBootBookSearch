DROP FUNCTION IF EXISTS search_books(
	sort_column VARCHAR(50),
	sort_order VARCHAR(50),
    search_term TEXT,
    min_created_at TIMESTAMPTZ,
	max_created_at TIMESTAMPTZ,
    limit_rows INT
);

CREATE OR REPLACE FUNCTION search_books(
	page INT DEFAULT 0,
	sort_column VARCHAR(50) DEFAULT 'created_at',
	sort_order VARCHAR(50) DEFAULT 'DESC',
    search_term TEXT DEFAULT '',
    min_created_at TIMESTAMPTZ DEFAULT '-infinity',
	max_created_at TIMESTAMPTZ DEFAULT 'infinity',
    limit_rows INT DEFAULT 20
)
RETURNS TABLE (
    id UUID,
    title VARCHAR,
    description VARCHAR,
    author VARCHAR,
	copies INT,
	rating REAL,
    created_at TIMESTAMPTZ,
    rank REAL
) AS $$
DECLARE
    sql TEXT;
	split_terms TEXT[];
	ts_query TEXT;
BEGIN
	
	split_terms := regexp_split_to_array(COALESCE(search_term, ''), '\s+');
	ts_query := array_to_string(split_terms, ' | ');
	sql := format($f$
		SELECT id, title, description, author, copies, rating, created_at,
			   ts_rank(document, plainto_tsquery('english', %L)) AS rank
		FROM book
		WHERE document @@ plainto_tsquery('english', %L) 
	$f$, ts_query, ts_query, split_terms);

	sql := sql || ' OR ';
	IF array_length(split_terms, 1) = 0 THEN
		sql := sql || format ('title ILIKE %L OR author ILIKE %L OR description ILIKE %L', '%', '%', '%');
	ELSE
		FOR i IN 1 .. array_length(split_terms, 1) LOOP
			IF split_terms[i] = '' THEN
				RAISE NOTICE 'empty string';
				sql := sql || format ('title ILIKE %L OR author ILIKE %L OR description ILIKE %L', '%', '%', '%');
			ELSE
				sql := sql || format('title ILIKE %L OR author ILIKE %L OR description ILIKE %L', '%' || split_terms[i] || '%', '%' || split_terms[i] || '%', '%' || split_terms[i] || '%');
			END IF;
		END LOOP;
	END IF;

	
	-- ranged query
	sql := sql || format($f$
		AND created_at >= %L AND created_at <= %L
		$f$, COALESCE(min_created_at, '-infinity'::timestamptz), COALESCE(max_created_at, 'infinity'::timestamptz));
	sort_column := COALESCE(sort_column, 'created_at');
	sort_order := COALESCE(sort_order,'DESC');
	IF sort_column = 'created_at' THEN
		IF sort_order = 'DESC' THEN 
			sql := sql ||
				format ('ORDER BY rank DESC, created_at DESC
				LIMIT 20 OFFSET %L', page * 20);
		ELSIF sort_order = 'ASC' THEN 
			sql := sql ||
				format ('ORDER BY rank DESC, created_at ASC
				LIMIT 20 OFFSET %L', page * 20);
		END IF;
	END IF;
	
	-- Return query result dynamically
	RETURN QUERY EXECUTE sql;
END;
$$ LANGUAGE plpgsql;

SELECT * FROM search_books(
0,
'created_at',
'DESC', 
'this',
NULL, 
NULL, 
10);
