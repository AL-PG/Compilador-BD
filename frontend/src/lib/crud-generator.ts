import type { TableDef } from '../types/compiler'

export function generateCrudHtml(databaseName: string, table: TableDef, apiBaseUrl: string): string {
  const pkColumn = table.columns.find((c) => c.primaryKey)
  const pkName = pkColumn?.name ?? ''
  const nonPkColumns = table.columns.filter((c) => !c.primaryKey)

  const inputFields = nonPkColumns
    .map((col) => {
      const inputType = col.type === 'numero' ? 'number' : col.type === 'fecha' ? 'date' : 'text'
      return `
      <div class="field">
        <label>${col.name}</label>
        <input type="${inputType}" name="${col.name}" id="fld-${col.name}" />
      </div>`
    })
    .join('\n')

  const tableHeaders = table.columns.map((col) => `<th>${col.name}${col.primaryKey ? ' (PK)' : ''}</th>`).join('')

  const apiPrefix = `${apiBaseUrl}/api/crud/${encodeURIComponent(databaseName)}/${encodeURIComponent(table.name)}`

  const html = `<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>CRUD - ${table.name}</title>
<style>
  * { box-sizing: border-box; }
  body { font-family: system-ui, -apple-system, Segoe UI, Roboto, Helvetica, Arial, sans-serif; margin: 0; padding: 24px; background: #f8f9fa; color: #111; }
  h1 { margin: 0 0 16px; font-size: 22px; }
  .card { background: #fff; border: 1px solid #e5e7eb; border-radius: 12px; padding: 16px; margin-bottom: 16px; }
  .toolbar { display: flex; gap: 8px; flex-wrap: wrap; margin-bottom: 12px; }
  button { border: 1px solid #e5e7eb; background: #fff; padding: 8px 12px; border-radius: 8px; cursor: pointer; font-size: 14px; }
  button.primary { background: #111; color: #fff; border-color: #111; }
  button.danger { color: #b91c1c; border-color: #fecaca; }
  button:hover { opacity: 0.9; }
  table { width: 100%; border-collapse: collapse; font-size: 14px; }
  th, td { border: 1px solid #e5e7eb; padding: 10px; text-align: left; }
  th { background: #f3f4f6; }
  .field { margin-bottom: 10px; }
  .field label { display: block; font-size: 12px; font-weight: 600; margin-bottom: 4px; text-transform: uppercase; color: #6b7280; }
  .field input { width: 100%; padding: 8px 10px; border: 1px solid #d1d5db; border-radius: 8px; }
  .hidden { display: none; }
  .status { font-size: 13px; padding: 10px; border-radius: 8px; margin-top: 10px; }
  .status.ok { background: #d1fae5; color: #065f46; }
  .status.err { background: #fee2e2; color: #991b1b; }
  .loading { color: #6b7280; font-size: 14px; padding: 10px 0; }
</style>
</head>
<body>
  <h1>CRUD: ${table.name}</h1>
  <div class="card">
    <div class="toolbar">
      <button class="primary" onclick="showForm()">+ Crear</button>
      <button onclick="loadData()">Refrescar</button>
    </div>
    <div id="formPanel" class="hidden card">
      <h3 id="formTitle">Nuevo registro</h3>
      <form id="crudForm" onsubmit="return onSubmit(event)">
        ${pkName ? `<div class="field"><label>${pkName} (PK)</label><input type="text" name="${pkName}" id="fld-${pkName}" /></div>` : ''}
        ${inputFields}
        <div class="toolbar" style="margin-top:12px;">
          <button class="primary" type="submit">Guardar</button>
          <button type="button" onclick="hideForm()">Cancelar</button>
        </div>
      </form>
      <div id="formStatus" class="status hidden"></div>
    </div>
    <div id="tableWrap">
      <div id="loading" class="loading">Cargando registros...</div>
      <table id="dataTable" class="hidden">
        <thead><tr>${tableHeaders}<th>Acciones</th></tr></thead>
        <tbody id="tbody"></tbody>
      </table>
    </div>
    <div id="status" class="status hidden"></div>
  </div>

<script>
  const API = '${apiPrefix}';
  const PK = '${pkName}';
  const columns = ${JSON.stringify(table.columns)};

  function showStatus(msg, ok) {
    const el = document.getElementById('status');
    el.textContent = msg;
    el.className = 'status ' + (ok ? 'ok' : 'err');
    el.classList.remove('hidden');
  }

  function showForm(record) {
    document.getElementById('formPanel').classList.remove('hidden');
    document.getElementById('formTitle').textContent = record ? 'Editar registro' : 'Nuevo registro';
    const form = document.getElementById('crudForm');
    form.reset();
    if (record) {
      columns.forEach(c => {
        const el = document.getElementById('fld-' + c.name);
        if (el) el.value = record[c.name] ?? '';
      });
      form.dataset.id = record[PK] ?? '';
    } else {
      delete form.dataset.id;
    }
    document.getElementById('formStatus').classList.add('hidden');
  }

  function hideForm() {
    document.getElementById('formPanel').classList.add('hidden');
  }

  async function loadData() {
    document.getElementById('status').classList.add('hidden');
    document.getElementById('loading').classList.remove('hidden');
    document.getElementById('dataTable').classList.add('hidden');
    try {
      const res = await fetch(API);
      if (!res.ok) throw new Error('HTTP ' + res.status);
      const data = await res.json();
      renderTable(data);
    } catch (e) {
      showStatus('Error al cargar datos: ' + e.message, false);
    } finally {
      document.getElementById('loading').classList.add('hidden');
    }
  }

  function renderTable(rows) {
    const tbody = document.getElementById('tbody');
    const table = document.getElementById('dataTable');
    tbody.innerHTML = '';
    table.classList.remove('hidden');
    if (!rows || rows.length === 0) {
      tbody.innerHTML = '<tr><td colspan="' + (columns.length + 1) + '">Sin registros</td></tr>';
      return;
    }
    rows.forEach(row => {
      const tr = document.createElement('tr');
      columns.forEach(c => {
        const td = document.createElement('td');
        td.textContent = row[c.name] ?? '';
        tr.appendChild(td);
      });
      const tdActions = document.createElement('td');
      tdActions.innerHTML = '<button onclick="editRow(' + JSON.stringify(row).replace(/"/g, '&quot;') + ')">Editar</button> <button class="danger" onclick="deleteRow(' + String(row[PK]).replace(/"/g, '&quot;') + ')">Eliminar</button>';
      tr.appendChild(tdActions);
      tbody.appendChild(tr);
    });
  }

  function editRow(record) {
    showForm(record);
  }

  async function deleteRow(id) {
    if (!confirm('Eliminar registro con ' + PK + ' = ' + id + '?')) return;
    try {
      const res = await fetch(API + '/' + encodeURIComponent(id), { method: 'DELETE' });
      if (!res.ok) throw new Error('HTTP ' + res.status);
      showStatus('Registro eliminado.', true);
      loadData();
    } catch (e) {
      showStatus('Error al eliminar: ' + e.message, false);
    }
  }

  async function onSubmit(e) {
    e.preventDefault();
    const form = e.target;
    const payload = {};
    columns.forEach(c => {
      const el = document.getElementById('fld-' + c.name);
      if (!el) return;
      let value = el.value;
      if (c.type === 'numero') {
        const num = parseFloat(value);
        payload[c.name] = isNaN(num) ? null : num;
      } else {
        payload[c.name] = value;
      }
    });

    if (PK && !payload[PK]) {
      showFormStatus('La clave primaria ' + PK + ' es obligatoria.', false);
      return false;
    }

    const id = form.dataset.id;
    try {
      let res;
      if (id) {
        res = await fetch(API + '/' + encodeURIComponent(id), {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(payload)
        });
      } else {
        res = await fetch(API, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(payload)
        });
      }
      if (!res.ok) {
        const text = await res.text().catch(() => 'Error ' + res.status);
        throw new Error(text || 'HTTP ' + res.status);
      }
      showStatus(id ? 'Registro actualizado.' : 'Registro creado.', true);
      hideForm();
      loadData();
    } catch (err) {
      showStatus('Error al guardar: ' + err.message, false);
    }
    return false;
  }

  function showFormStatus(msg, ok) {
    const el = document.getElementById('formStatus');
    el.textContent = msg;
    el.className = 'status ' + (ok ? 'ok' : 'err');
    el.classList.remove('hidden');
  }

  loadData();
</script>
</body>
</html>`

  return html
}
