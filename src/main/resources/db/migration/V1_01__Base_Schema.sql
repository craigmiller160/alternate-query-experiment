CREATE
    EXTENSION pg_crypto;

CREATE
    TABLE
        positions(
            id UUID NOT NULL,
            name VARCHAR(255),
            PRIMARY KEY(id)
        );

INSERT
    INTO
        positions(
            id,
            name
        )
    VALUES(
        gen_random_uuid(),
        'Supervisor'
    ),
    (
        gen_random_uuid(),
        'Worker'
    );

CREATE
    TABLE
        employees(
            id UUID NOT NULL,
            first_name VARCHAR(255),
            last_name VARCHAR(255),
            date_of_birth DATE,
            position_id UUID NOT NULL,
            PRIMARY KEY(id),
            FOREIGN KEY(position_id) REFERENCES positions(id)
        );

CREATE
    FUNCTION is_supervisor(
        employee_id UUID
    ) RETURNS BOOLEAN AS $$ SELECT
        COUNT(*)> 0
    FROM
        employees
    JOIN positions ON
        employees.position_id = positions.id
    WHERE
        employees.id = employee_id
        AND positions.name = 'Supervisor' $$ LANGUAGE SQL;

CREATE
    TABLE
        teams(
            id UUID NOT NULL,
            supervisor_id UUID NOT NULL,
            PRIMARY KEY(id),
            FOREIGN KEY(supervisor_id) REFERENCES employees(id),
            CHECK(
                is_supervisor(supervisor_id)
            )
        );

CREATE
    TABLE
        team_members(
            id UUID NOT NULL,
            team_id UUID NOT NULL,
            employee_id UUID NOT NULL,
            PRIMARY KEY(id),
            FOREIGN KEY(team_id) REFERENCES teams(id),
            FOREIGN KEY(employee_id) REFERENCES employees(id)
        );