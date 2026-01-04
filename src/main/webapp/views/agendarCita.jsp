<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Agendar Cita Médica</title>
</head>
<body>

<h2>Agendar Cita Médica</h2>

<form action="../agendarCita?accion=agendarCita" method="post">
    Fecha: <input type="date" name="fecha" required><br><br>
    Hora: <input type="time" name="hora" required><br><br>
    Motivo: <input type="text" name="motivo" required><br><br>
    <button type="submit">Agendar</button>
</form>

</body>
</html>