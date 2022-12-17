CREATE TABLE IF NOT EXISTS candidate (
   id SERIAL PRIMARY KEY,
   name TEXT,
   description TEXT,
   created DATE,
   city_id INTEGER
);