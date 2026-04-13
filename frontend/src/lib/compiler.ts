export const SAMPLE_PROGRAM = `crear base TiendaOnline;

tabla Clientes con:
    nombre como texto,
    telefono como numero,
    nacimiento como fecha;

tabla Productos con:
    codigo_barras como numero,
    descripcion como texto;

tabla Pedidos con:
    folio como numero,
    fecha_compra como fecha,
    estatus como texto;`