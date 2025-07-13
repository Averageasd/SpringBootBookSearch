CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE TABLE IF NOT EXISTS book (
	id UUID PRIMARY KEY DEFAULT uuid_generate_v1(),
	title VARCHAR(50) NOT NULL,
	description VARCHAR(250) NOT NULL,
	created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
	copies INT NOT NULL, 
	rating REAL NOT NULL,
	author VARCHAR(50) NOT NULL,

	CONSTRAINT unique_title UNIQUE (title),
	CONSTRAINT chk_title_length CHECK (char_length(title) >= 3),
	CONSTRAINT chk_description_length CHECK (char_length(description) >= 1),
	CONSTRAINT chk_author_length CHECK (char_length(author) >= 1)
);

ALTER TABLE book
ADD COLUMN document tsvector GENERATED ALWAYS AS (
    setweight(to_tsvector('english', coalesce(title, '')), 'A') ||
    setweight(to_tsvector('english', coalesce(author, '')), 'A') ||
    setweight(to_tsvector('english', coalesce(description, '')), 'B')
) STORED;

CREATE INDEX IF NOT EXISTS idx_book_fulltext ON book USING gin (document);
CREATE INDEX IF NOT EXISTS idx_book_id ON book (id);
CREATE INDEX IF NOT EXISTS idx_book_copies ON book (copies);
CREATE INDEX IF NOT EXISTS idx_book_rating ON book (rating);
CREATE INDEX IF NOT EXISTS idx_book_createdAt ON book (created_at);
CREATE INDEX IF NOT EXISTS idx_book_title ON book (title);
CREATE INDEX IF NOT EXISTS idx_book_author ON book (author);

CREATE INDEX IF NOT EXISTS book_title_trgm_idx ON book USING gin (title gin_trgm_ops);
CREATE INDEX IF NOT EXISTS book_author_trgm_idx ON book USING gin (author gin_trgm_ops);
CREATE INDEX IF NOT EXISTS book_description_trgm_idx ON book USING gin (description gin_trgm_ops);
