DROP FUNCTION IF EXISTS search_books(
	page integer,
	sort_column character varying,
	sort_order character varying,
	search_term text,
	min_created_at timestamp,
	max_created_at timestamp,
	min_copies integer,
	max_copies integer,
	min_rating real,
	max_rating real,
	limit_rows integer
);

CREATE OR REPLACE FUNCTION public.search_books(
	page integer DEFAULT 0,
	sort_column character varying DEFAULT 'created_at'::character varying,
	sort_order character varying DEFAULT 'DESC'::character varying,
	search_term text DEFAULT ''::text,
	min_created_at timestamp DEFAULT '-infinity'::timestamp,
	max_created_at timestamp DEFAULT 'infinity'::timestamp,
	min_copies integer DEFAULT 0,
	max_copies integer DEFAULT 0,
	min_rating real DEFAULT 0.0,
	max_rating real DEFAULT 5.0,
	limit_rows integer DEFAULT 20)
    RETURNS TABLE(id uuid, title character varying, description character varying, author character varying, copies integer, rating real, created_at timestamp without time zone, rank real) 
    LANGUAGE 'plpgsql'

AS $BODY$
DECLARE
    sql TEXT;
	split_terms TEXT[];
	ts_query TEXT;
BEGIN
	split_terms := regexp_split_to_array(COALESCE(search_term, ''), '\s+');
	ts_query := array_to_string(split_terms, ' | ');
	RAISE NOTICE 'ts_query here %', ts_query;
	RAISE NOTICE 'split_terms %', split_terms;
	sql := format('
		SELECT id, title, description, author, copies, rating, created_at,
			   ts_rank(document, plainto_tsquery(%L)) AS rank
		FROM book
		WHERE (document @@ plainto_tsquery(%L)', ts_query, ts_query);

	IF array_length(split_terms, 1) = 0 THEN
		sql := sql || format (' OR title ILIKE %L OR author ILIKE %L OR description ILIKE %L', '%', '%', '%');
	ELSE
		FOR i IN 1 .. array_length(split_terms, 1) LOOP
			IF split_terms[i] = '' THEN
				RAISE NOTICE 'empty string';
				sql := sql || format (' OR title ILIKE %L OR author ILIKE %L OR description ILIKE %L', '%', '%', '%');
			ELSE
				sql := sql || format(' OR title ILIKE %L OR author ILIKE %L OR description ILIKE %L', '%' || split_terms[i] || '%', '%' || split_terms[i] || '%', '%' || split_terms[i] || '%');
			END IF;
		END LOOP;
	END IF;

	sql := sql || ')';

	sql := sql || format($f$
		AND created_at >= %L AND created_at <= %L
		$f$, COALESCE(min_created_at, '-infinity'::timestamptz), COALESCE(max_created_at, 'infinity'::timestamptz));
	sort_column := COALESCE(sort_column, 'created_at');
	sort_order := COALESCE(sort_order,'DESC');

	RAISE NOTICE 'min copies % ', min_copies;
	RAISE NOTICE 'max copies % ',  max_copies;
	IF min_copies IS NOT NULL AND max_copies IS NOT NULL THEN
		
		IF min_copies = max_copies THEN
			sql:= sql || format($f$ AND copies >= %s $f$, min_copies);
		ELSIF max_copies > min_copies THEN
			RAISE NOTICE 'execute this part for ranged min_copies.';
			sql:= sql || format($f$ AND copies >= %s AND copies <= %s $f$, min_copies, max_copies);
		END IF;
	END IF;

	IF min_rating IS NOT NULL AND max_rating IS NOT NULL THEN
		IF min_rating = max_rating THEN
			sql:= sql || format($f$ AND rating >= %s $f$, min_rating);
		ELSIF max_rating > min_rating THEN
			sql:= sql || format($f$ AND rating >= %s AND rating <= %s $f$, min_rating, max_rating);
		END IF;
	END IF;
	
	-- ranged query
	IF sort_column = 'created_at' THEN
		IF sort_order = 'DESC' THEN 
			sql := sql ||
				format ('ORDER BY rank DESC, created_at DESC
				LIMIT 20 OFFSET %s', page * 20);
		ELSIF sort_order = 'ASC' THEN 
			sql := sql ||
				format ('ORDER BY rank DESC, created_at ASC
				LIMIT 20 OFFSET %s', page * 20);
				
		END IF;
	END IF;
	
	IF sort_column = 'copies' THEN
		IF sort_order = 'DESC' THEN 
			sql := sql ||
				format ('ORDER BY rank DESC, copies DESC
				LIMIT 20 OFFSET %s', page * 20);
		ELSIF sort_order = 'ASC' THEN 
			sql := sql ||
				format ('ORDER BY rank DESC, copies ASC
				LIMIT 20 OFFSET %s', page * 20);
		END IF;
	END IF;

	IF sort_column = 'rating' THEN
		IF sort_order = 'DESC' THEN 
			sql := sql ||
				format ('ORDER BY rank DESC, rating DESC
				LIMIT 20 OFFSET %s', page * 20);
		ELSIF sort_order = 'ASC' THEN 
			sql := sql ||
				format ('ORDER BY rank DESC, rating ASC
				LIMIT 20 OFFSET %s', page * 20);
		END IF;
	END IF;

	IF sort_column = 'title' THEN
		IF sort_order = 'DESC' THEN 
			sql := sql ||
				format ('ORDER BY rank DESC, title DESC
				LIMIT 20 OFFSET %s', page * 20);
		ELSIF sort_order = 'ASC' THEN 
			sql := sql ||
				format ('ORDER BY rank DESC, title ASC
				LIMIT 20 OFFSET %s', page * 20);
		END IF;
	END IF;

	RAISE NOTICE '%', sql;
	-- Return query result dynamically
	RETURN QUERY EXECUTE sql;
END;
$BODY$;

SELECT search_books(
0, 
'copies', 
'DESC', 
'agent every', 
'2025-07-01 00:00:00'::timestamp,
'2025-07-24 23:59:59'::timestamp,
1,
5,
0,
5);

