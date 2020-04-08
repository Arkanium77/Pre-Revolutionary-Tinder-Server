CREATE TABLE IF NOT EXISTS public.roles
(
    id        integer                NOT NULL,
    role_name character varying(250) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO public.roles
VALUES (0, 'User')
ON CONFLICT DO NOTHING;
INSERT INTO public.roles
VALUES (1, 'Admin')
ON CONFLICT DO NOTHING;