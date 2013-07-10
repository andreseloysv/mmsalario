CREATE TABLE demo.ciudad(
  ciudad_id INTEGER NOT NULL,
  ciudad VARCHAR(50) NOT NULL
);
COMMENT ON TABLE demo.ciudad IS 'Ciudades';
COMMENT ON COLUMN demo.ciudad.ciudad_id IS 'Clave Primaria';
COMMENT ON COLUMN demo.ciudad.ciudad IS 'Ciudad';

CREATE SEQUENCE demo.seq_ciudad;

CREATE TABLE demo.afiliado(
  afiliado_id INTEGER NOT NULL,
  nombre VARCHAR(60) NOT NULL,
  cedula VARCHAR(20) NOT NULL,
  telefono VARCHAR(20) NOT NULL,
  email VARCHAR(60),
  sexo CHARACTER(1) NOT NULL,
  ciudad_id INTEGER NOT NULL
);
COMMENT ON TABLE demo.afiliado IS 'Afiliación';
COMMENT ON COLUMN demo.afiliado.afiliado_id IS 'Clave primaria';
COMMENT ON COLUMN demo.afiliado.nombre IS 'Nombre';
COMMENT ON COLUMN demo.afiliado.cedula IS 'Cédula';
COMMENT ON COLUMN demo.afiliado.telefono IS 'Télefono';
COMMENT ON COLUMN demo.afiliado.email IS 'Email';
COMMENT ON COLUMN demo.afiliado.sexo IS 'Sexo @{M=Masculino|F=Femenino}';
COMMENT ON COLUMN demo.afiliado.ciudad_id IS 'Ciudad';

CREATE SEQUENCE demo.seq_afiliado;

CREATE TABLE demo.familiar(
  familiar_id INTEGER NOT NULL,
  nombre VARCHAR(60) NOT NULL,
  filiacion VARCHAR(10) NOT NULL,
  fnacimiento TIMESTAMP NOT NULL,
  telefono VARCHAR(20) NOT NULL,
  afiliado_id INTEGER NOT NULL
);
COMMENT ON TABLE demo.familiar IS 'Familiar';
COMMENT ON COLUMN demo.familiar.familiar_id IS 'Clave Primaria';
COMMENT ON COLUMN demo.familiar.nombre IS 'Nombre';
COMMENT ON COLUMN demo.familiar.filiacion IS 'Filiación @{Madre=Madre|Padre=Padre|Hermano=Hermano|Hijo=Hijo|Otro=Otro}';
COMMENT ON COLUMN demo.familiar.fnacimiento IS 'Fecha nacimiento';
COMMENT ON COLUMN demo.familiar.telefono IS 'Teléfono';
COMMENT ON COLUMN demo.familiar.afiliado_id IS 'Afiliado';

CREATE SEQUENCE demo.seq_familiar;

ALTER TABLE demo.ciudad ADD PRIMARY KEY (ciudad_id);

ALTER TABLE demo.afiliado ADD PRIMARY KEY (afiliado_id);
ALTER TABLE demo.afiliado ADD CONSTRAINT FK_afiliado_0 FOREIGN KEY (ciudad_id) REFERENCES demo.ciudad (ciudad_id) ON DELETE CASCADE;

ALTER TABLE demo.familiar ADD PRIMARY KEY (familiar_id);
ALTER TABLE demo.familiar ADD CONSTRAINT FK_familiar_0 FOREIGN KEY (afiliado_id) REFERENCES demo.afiliado (afiliado_id) ON DELETE CASCADE;

INSERT INTO demo.ciudad(ciudad_id, ciudad) VALUES (1, 'Caracas');
INSERT INTO demo.ciudad(ciudad_id, ciudad) VALUES (2, 'La Guaira');
INSERT INTO demo.ciudad(ciudad_id, ciudad) VALUES (3, 'Cumaná');
INSERT INTO demo.ciudad(ciudad_id, ciudad) VALUES (4, 'Ciudad Bolivar');
INSERT INTO demo.ciudad(ciudad_id, ciudad) VALUES (5, 'Mérida');
