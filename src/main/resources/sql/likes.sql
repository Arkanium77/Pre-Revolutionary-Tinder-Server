CREATE TABLE IF NOT EXISTS public.likes
(
    id   bigserial NOT NULL,
    who  uuid      NOT NULL,
    whom uuid      NOT NULL,
    PRIMARY KEY (id)
);