# 🧪 Guía de Pruebas - Ticket Service API

## 🚀 Iniciar el Servidor

```bash
cd ticket-service
.\mvnw spring-boot:run
```

El servidor estará disponible en: `http://localhost:8081`

---

## 📋 Ejemplos de Peticiones

### 1. Crear un Ticket (POST)

**PowerShell:**
```powershell
$body = @{
    titulo = "Error en el login"
    descripcion = "El botón de inicio de sesión no responde al hacer clic"
    prioridad = "ALTA"
    estado = "ABIERTO"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8081/api/tickets" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body
```

**cURL (Windows CMD):**
```cmd
curl -X POST http://localhost:8081/api/tickets ^
  -H "Content-Type: application/json" ^
  -d "{\"titulo\":\"Error en el login\",\"descripcion\":\"El botón no responde\",\"prioridad\":\"ALTA\",\"estado\":\"ABIERTO\"}"
```

**Respuesta esperada (201 Created):**
```json
{
  "id": 1,
  "titulo": "Error en el login",
  "descripcion": "El botón de inicio de sesión no responde al hacer clic",
  "prioridad": "ALTA",
  "estado": "ABIERTO",
  "fechaCreacion": "2026-01-30T01:30:00"
}
```

---

### 2. Obtener Todos los Tickets (GET)

**PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8081/api/tickets" -Method GET
```

**cURL:**
```cmd
curl http://localhost:8081/api/tickets
```

**Respuesta esperada (200 OK):**
```json
[
  {
    "id": 1,
    "titulo": "Error en el login",
    "descripcion": "El botón no responde",
    "prioridad": "ALTA",
    "estado": "ABIERTO",
    "fechaCreacion": "2026-01-30T01:30:00"
  },
  {
    "id": 2,
    "titulo": "Mejora en UI",
    "descripcion": "Agregar animaciones",
    "prioridad": "BAJA",
    "estado": "EN_PROCESO",
    "fechaCreacion": "2026-01-30T01:31:00"
  }
]
```

---

### 3. Obtener un Ticket por ID (GET)

**PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8081/api/tickets/1" -Method GET
```

**cURL:**
```cmd
curl http://localhost:8081/api/tickets/1
```

**Respuesta esperada (200 OK):**
```json
{
  "id": 1,
  "titulo": "Error en el login",
  "descripcion": "El botón no responde",
  "prioridad": "ALTA",
  "estado": "ABIERTO",
  "fechaCreacion": "2026-01-30T01:30:00"
}
```

**Si no existe (404 Not Found):**
```
(Sin contenido)
```

---

### 4. Actualizar un Ticket (PUT)

**PowerShell:**
```powershell
$body = @{
    titulo = "Error en el login - RESUELTO"
    descripcion = "Se corrigió el evento onClick"
    prioridad = "ALTA"
    estado = "CERRADO"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8081/api/tickets/1" `
    -Method PUT `
    -ContentType "application/json" `
    -Body $body
```

**cURL:**
```cmd
curl -X PUT http://localhost:8081/api/tickets/1 ^
  -H "Content-Type: application/json" ^
  -d "{\"titulo\":\"Error en el login - RESUELTO\",\"descripcion\":\"Se corrigió el evento onClick\",\"prioridad\":\"ALTA\",\"estado\":\"CERRADO\"}"
```

**Respuesta esperada (200 OK):**
```json
{
  "id": 1,
  "titulo": "Error en el login - RESUELTO",
  "descripcion": "Se corrigió el evento onClick",
  "prioridad": "ALTA",
  "estado": "CERRADO",
  "fechaCreacion": "2026-01-30T01:30:00"
}
```

---

### 5. Eliminar un Ticket (DELETE)

**PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8081/api/tickets/1" -Method DELETE
```

**cURL:**
```cmd
curl -X DELETE http://localhost:8081/api/tickets/1
```

**Respuesta esperada (204 No Content):**
```
(Sin contenido - eliminado exitosamente)
```

---

## ❌ Ejemplos de Errores de Validación

### Crear ticket sin título (400 Bad Request)

**PowerShell:**
```powershell
$body = @{
    descripcion = "Descripción sin título"
    prioridad = "MEDIA"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8081/api/tickets" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body
```

**Respuesta esperada (400 Bad Request):**
```json
{
  "timestamp": "2026-01-30T01:35:00",
  "status": 400,
  "error": "Error de validación",
  "errors": {
    "titulo": "El título no puede estar vacío",
    "estado": "El estado es obligatorio"
  }
}
```

---

## 🔍 Valores Válidos para Enums

### Prioridad
- `BAJA`
- `MEDIA`
- `ALTA`

### Estado
- `ABIERTO`
- `EN_PROCESO`
- `CERRADO`

---

## 📱 Ejemplo desde Flutter

```dart
import 'dart:convert';
import 'package:http/http.dart' as http;

class TicketService {
  final String baseUrl = 'http://localhost:8081/api/tickets';

  // Crear ticket
  Future<Map<String, dynamic>> createTicket({
    required String titulo,
    required String descripcion,
    required String prioridad,
    String estado = 'ABIERTO',
  }) async {
    final response = await http.post(
      Uri.parse(baseUrl),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'titulo': titulo,
        'descripcion': descripcion,
        'prioridad': prioridad,
        'estado': estado,
      }),
    );

    if (response.statusCode == 201) {
      return jsonDecode(response.body);
    } else {
      throw Exception('Error al crear ticket: ${response.body}');
    }
  }

  // Obtener todos los tickets
  Future<List<dynamic>> getAllTickets() async {
    final response = await http.get(Uri.parse(baseUrl));

    if (response.statusCode == 200) {
      return jsonDecode(response.body);
    } else {
      throw Exception('Error al obtener tickets');
    }
  }

  // Actualizar ticket
  Future<Map<String, dynamic>> updateTicket(
    int id, {
    required String titulo,
    required String descripcion,
    required String prioridad,
    required String estado,
  }) async {
    final response = await http.put(
      Uri.parse('$baseUrl/$id'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'titulo': titulo,
        'descripcion': descripcion,
        'prioridad': prioridad,
        'estado': estado,
      }),
    );

    if (response.statusCode == 200) {
      return jsonDecode(response.body);
    } else if (response.statusCode == 404) {
      throw Exception('Ticket no encontrado');
    } else {
      throw Exception('Error al actualizar ticket');
    }
  }

  // Eliminar ticket
  Future<void> deleteTicket(int id) async {
    final response = await http.delete(Uri.parse('$baseUrl/$id'));

    if (response.statusCode != 204) {
      throw Exception('Error al eliminar ticket');
    }
  }
}

// Uso:
void main() async {
  final service = TicketService();

  // Crear ticket
  final ticket = await service.createTicket(
    titulo: 'Bug en Flutter',
    descripcion: 'Error al renderizar widget',
    prioridad: 'ALTA',
  );
  print('Ticket creado: ${ticket['id']}');

  // Obtener todos
  final tickets = await service.getAllTickets();
  print('Total de tickets: ${tickets.length}');
}
```

---

## 🛠️ Herramientas Recomendadas

1. **Postman**: Para pruebas interactivas de API
2. **Thunder Client** (VS Code Extension): Cliente REST integrado
3. **curl**: Para scripts automatizados
4. **PowerShell**: Para pruebas rápidas en Windows

---

## 📝 Notas Importantes

- ✅ El servidor debe estar corriendo en el puerto **8081**
- ✅ CORS está configurado, no habrá problemas con Flutter
- ✅ Las validaciones son automáticas (Bean Validation)
- ✅ Los errores devuelven JSON estructurado
- ✅ `fechaCreacion` se asigna automáticamente al crear un ticket
- ✅ Si no se especifica `estado`, se asigna `ABIERTO` por defecto
