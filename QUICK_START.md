# 🚀 Guía Rápida de Inicio

## ✅ Problema Resuelto

El error de PostgreSQL ha sido solucionado. Ahora la aplicación usa **H2** (base de datos en memoria) por defecto.

---

## 🏃 Iniciar el Servidor

```powershell
.\mvnw spring-boot:run
```

**Resultado esperado:**
```
Started TicketServiceApplication in 2.3 seconds ✅
```

---

## 🧪 Probar la API

### 1. Ver todos los tickets (GET)
```powershell
Invoke-RestMethod -Uri "http://localhost:8081/api/tickets" -Method GET
```

### 2. Crear un ticket (POST)
```powershell
$body = @{
    titulo = "Mi primer ticket"
    descripcion = "Descripción del problema"
    prioridad = "ALTA"
    estado = "ABIERTO"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8081/api/tickets" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body
```

### 3. Ver la base de datos (H2 Console)
Abre en tu navegador: **http://localhost:8081/h2-console**

**Credenciales:**
- JDBC URL: `jdbc:h2:mem:ticketdb`
- Username: `sa`
- Password: *(vacío)*

---

## 🔄 Cambiar a PostgreSQL (Opcional)

Si quieres usar PostgreSQL en lugar de H2:

1. **Asegúrate de tener PostgreSQL corriendo**
2. **Crea la base de datos:**
   ```sql
   CREATE DATABASE ticket_db;
   ```
3. **Ejecuta con el perfil prod:**
   ```powershell
   .\mvnw spring-boot:run -Dspring-boot.run.profiles=prod
   ```

---

## 📱 Conectar desde Flutter

```dart
import 'package:http/http.dart' as http;
import 'dart:convert';

// Obtener todos los tickets
final response = await http.get(
  Uri.parse('http://localhost:8081/api/tickets'),
);

// Crear un ticket
final createResponse = await http.post(
  Uri.parse('http://localhost:8081/api/tickets'),
  headers: {'Content-Type': 'application/json'},
  body: jsonEncode({
    'titulo': 'Bug en Flutter',
    'descripcion': 'Error al renderizar',
    'prioridad': 'ALTA',
    'estado': 'ABIERTO',
  }),
);
```

---

## 📋 Valores Válidos

### Prioridad
- `BAJA`
- `MEDIA`
- `ALTA`

### Estado
- `ABIERTO`
- `EN_PROCESO`
- `CERRADO`

---

## 🎯 Endpoints Disponibles

| Método | URL | Descripción |
|--------|-----|-------------|
| GET | `/api/tickets` | Lista todos los tickets |
| GET | `/api/tickets/{id}` | Obtiene un ticket |
| POST | `/api/tickets` | Crea un ticket |
| PUT | `/api/tickets/{id}` | Actualiza un ticket |
| DELETE | `/api/tickets/{id}` | Elimina un ticket |

---

## 💡 Notas

- ✅ **H2** se reinicia cada vez que detienes el servidor (datos temporales)
- ✅ **CORS** está configurado para Flutter
- ✅ **Validaciones** automáticas en todos los campos
- ✅ **Logs** detallados en la consola

**¡Tu API está lista para usar!** 🎉
