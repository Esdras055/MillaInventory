# Inventario MILLA

## Integrantes
- **Omar Farid Parada Paredes** - **PP16018**
- **Esdras Leonel Peraza Pérez** - **PP24012**
- **Rodrigo Antonio Alvarado Pérez** - **AP23050**
- **Ricardo José Guevara Aldana** - **GA24023**

## Descripción del proyecto
La aplicación web Inventario MILLA resuelve la necesidad de la empresa Milla de llevar un control ordenado y confiable del inventario de mobiliario almacenado en sus distintas bodegas. Permite gestionar productos, categorías, marcas, proveedores y usuarios, además de registrar de forma precisa las entradas y salidas de inventario con su respectiva fecha, cantidad, proveedor y precio de adquisición. Con ello, se evita el descontrol de existencias, la pérdida de información y los errores manuales en los movimientos de productos. Asimismo, facilita la consulta de reportes clave para la toma de decisiones, como existencias por bodega e historial de movimientos. Al ser una aplicación responsive y contar con distintos niveles de acceso, también mejora la administración y seguridad del sistema.


## Diagrama Entidad - Relación

![DER](./database/der.png)

## Manual de despligue

Se necesita Docker Desktop con Docker Compose. Desde la raiz del proyecto:

```bash
docker compose up --build
```

La aplicacion queda disponible en `http://localhost:3000`, la API en
`http://localhost:8080` y Swagger en
`http://localhost:8080/swagger-ui.html`.

PostgreSQL ejecuta `database/MillaInventory.sql` automaticamente al crear el
volumen por primera vez. Para borrar la base, volver a crear las tablas y cargar
otra vez los datos iniciales:

```bash
docker compose down -v
docker compose up --build
```


