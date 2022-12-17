CREATE TABLE IF NOT EXISTS post (
   id SERIAL PRIMARY KEY,
   name TEXT,
   description TEXT,
   created DATE,
   city_id INTEGER
);