CREATE TABLE IF NOT EXISTS public.users
(
    id              uuid                   NOT NULL,
    username        character varying(250) NOT NULL,
    password        character varying(250) NOT NULL,
    role            integer                NOT NULL REFERENCES roles (Id),
    sex             bool                   NOT NULL,
    profile_message character varying(500),
    PRIMARY KEY (id)
);