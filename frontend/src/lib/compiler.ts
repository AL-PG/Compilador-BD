export const SAMPLE_PROGRAM = `crear base TiendaOnline;

tabla Clientes con:
    id como numero clave,
    nombre como texto,
    telefono como texto,
    nacimiento como fecha;

tabla Productos con:
    id como numero clave,
    codigo_barras como texto,
    descripcion como texto;

tabla Pedidos con:
    id como numero clave,
    folio como numero,
    fecha_compra como fecha,
    estatus como texto;`