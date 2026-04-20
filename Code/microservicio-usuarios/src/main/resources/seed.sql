-- Seed de datos iniciales para la base de datos de usuarios.
-- Ejecutar UNA VEZ después de que JPA haya creado las tablas (primer arranque del microservicio).
--
-- Credenciales del administrador:
--   Email : admin@segundum.com
--   Clave : admin123
--
-- Acceder al contenedor MySQL:
--   docker exec -it <nombre-contenedor-mysql> mysql -u root -proot db_usuarios
-- Y luego pegar este script.

INSERT INTO USUARIO (ID, EMAIL, NOMBRE, APELLIDOS, CLAVE, FECHA_NACIMIENTO, TELEFONO, ES_ADMIN, CONTADOR_COMPRAS, CONTADOR_VENTAS, GIT_HUB_ID)
VALUES ('admin-001', 'admin@segundum.com', 'Admin', 'Segundum', 'admin123', '1990-01-01', '000000000', 1, 0, 0, NULL);
