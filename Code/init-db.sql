/* Este fichero inicializa la base de datos al arrancar el container MySQL.
   Crea las dos bases de datos del proyecto, la tabla USUARIO con la
   estructura que Hibernate generaría, y carga usuarios de prueba. */

CREATE DATABASE IF NOT EXISTS db_usuarios;
CREATE DATABASE IF NOT EXISTS db_productos;

USE db_usuarios;

/* Creamos la tabla de usuarios para poder insertar datos de prueba */
CREATE TABLE IF NOT EXISTS USUARIO (
    ID              VARCHAR(255) NOT NULL,
    APELLIDOS       VARCHAR(255),
    CLAVE           LONGTEXT,
    CONTADORCOMPRAS INT,
    CONTADORVENTAS  INT,
    EMAIL           VARCHAR(255),
    ESADMIN         TINYINT(1) DEFAULT 0,
    FECHANACIMIENTO DATE,
    GITHUBID        VARCHAR(255),
    NOMBRE          VARCHAR(255),
    TELEFONO        VARCHAR(255),
    PRIMARY KEY (ID)
);

/* Usuarios de prueba, pues en el enunciado nos piden varios usuarios de prueba entre ellos uno administrador */

/* Administrador */
INSERT IGNORE INTO USUARIO (ID, EMAIL, NOMBRE, APELLIDOS, CLAVE, FECHANACIMIENTO, TELEFONO, ESADMIN, CONTADORCOMPRAS, CONTADORVENTAS)
VALUES ('admin-001', 'admin@segundum.com', 'Admin', 'Sistema', 'admin123', '1990-01-01', '600000000', 1, 0, 0);
/* Usuarios normales */
INSERT IGNORE INTO USUARIO (ID, EMAIL, NOMBRE, APELLIDOS, CLAVE, FECHANACIMIENTO, TELEFONO, ESADMIN, CONTADORCOMPRAS, CONTADORVENTAS)
VALUES ('user-001', 'alejandro@segundum.com', 'Alejandro', 'Carrion Jordan', 'alejandro123', '2004-01-29', '600111222', 0, 0, 0);
INSERT IGNORE INTO USUARIO (ID, EMAIL, NOMBRE, APELLIDOS, CLAVE, FECHANACIMIENTO, TELEFONO, ESADMIN, CONTADORCOMPRAS, CONTADORVENTAS)
VALUES ('user-002', 'borja@segundum.com', 'Borja', 'Sancho Fernandez', 'borja123', '2004-07-22', '600333444', 0, 0, 0);
INSERT IGNORE INTO USUARIO (ID, EMAIL, NOMBRE, APELLIDOS, CLAVE, FECHANACIMIENTO, TELEFONO, ESADMIN, CONTADORCOMPRAS, CONTADORVENTAS)
VALUES ('user-003', 'salvador@segundum.com', 'Salvador', 'Manzanares Guillen', 'salvador123', '2004-07-22', '600555666', 0, 0, 0);

/*Ahora los vamos a crear en la base de datos de productos para que puedan crear productos, cuando 
un usuario se registra esto no hace falta porque se hace solo pero en este caso si. */
USE db_productos;
/* Creamos una tabla para los usuarios que pueden crear productos */
CREATE TABLE IF NOT EXISTS usuario (
      id        VARCHAR(255) NOT NULL,
      email     VARCHAR(255),
      nombre    VARCHAR(255),
      apellidos VARCHAR(255),
      PRIMARY KEY (id)
  );

  /* Mismos usuarios que en db_usuarios, replicados para que puedan crear productos */
  INSERT IGNORE INTO usuario (id, email, nombre, apellidos) VALUES ('admin-001', 'admin@segundum.com', 'Admin',     'Sistema');
  INSERT IGNORE INTO usuario (id, email, nombre, apellidos) VALUES ('user-001',  'alejandro@segundum.com', 'Alejandro','Carrion Jordan');
  INSERT IGNORE INTO usuario (id, email, nombre, apellidos) VALUES ('user-002',  'borja@segundum.com',    'Borja',    'Sancho Fernandez');
  INSERT IGNORE INTO usuario (id, email, nombre, apellidos) VALUES ('user-003',  'salvador@segundum.com', 'Salvador', 'Manzanares Guillen');