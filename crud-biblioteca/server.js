const express = require('express');
const sqlite3 = require('sqlite3').verbose();
const cors = require('cors');

const app = express();
const port = 3000;

// Middleware
app.use(cors());
app.use(express.json());

// Conexión a la base de datos SQLite
const db = new sqlite3.Database('./biblioteca.db', (err) => {
    if (err) {
        console.error('Error al conectar con SQLite:', err.message);
    } else {
        console.log('Conectado a la base de datos SQLite.');
        
        // Crear tabla de libros
        db.run(`CREATE TABLE IF NOT EXISTS libros (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            titulo TEXT NOT NULL,
            autor TEXT NOT NULL
        )`);

        // Crear tabla de ejemplares con llave foránea
        db.run(`CREATE TABLE IF NOT EXISTS ejemplares (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            libro_id INTEGER NOT NULL,
            estado TEXT DEFAULT 'Disponible',
            FOREIGN KEY (libro_id) REFERENCES libros(id) ON DELETE CASCADE
        )`);
    }
});

// ================= RUTAS PARA LIBROS =================

// CREATE: Añadir un nuevo libro
app.post('/libros', (req, res) => {
    const { titulo, autor } = req.body;
    db.run(`INSERT INTO libros (titulo, autor) VALUES (?, ?)`, [titulo, autor], function(err) {
        if (err) return res.status(400).json({ error: err.message });
        res.json({ id: this.lastID, titulo, autor });
    });
});

// READ: Obtener todos los libros
app.get('/libros', (req, res) => {
    db.all(`SELECT * FROM libros`, [], (err, rows) => {
        if (err) return res.status(400).json({ error: err.message });
        res.json(rows);
    });
});

// UPDATE: Actualizar un libro
app.put('/libros/:id', (req, res) => {
    const { titulo, autor } = req.body;
    db.run(`UPDATE libros SET titulo = ?, autor = ? WHERE id = ?`, 
        [titulo, autor, req.params.id], 
        function(err) {
            if (err) return res.status(400).json({ error: err.message });
            res.json({ message: "Libro actualizado", changes: this.changes });
    });
});

// DELETE: Eliminar un libro
app.delete('/libros/:id', (req, res) => {
    db.run(`DELETE FROM libros WHERE id = ?`, req.params.id, function(err) {
        if (err) return res.status(400).json({ error: err.message });
        res.json({ message: "Libro eliminado", changes: this.changes });
    });
});

// ================= RUTAS PARA EJEMPLARES =================

// Crear un nuevo ejemplar
app.post('/ejemplares', (req, res) => {
    const { libro_id, estado } = req.body;
    db.run(`INSERT INTO ejemplares (libro_id, estado) VALUES (?, ?)`, 
        [libro_id, estado || 'Disponible'], 
        function(err) {
            if (err) return res.status(400).json({ error: err.message });
            res.json({ id: this.lastID, libro_id, estado });
    });
});

// Ver todos los ejemplares de un libro
app.get('/libros/:id/ejemplares', (req, res) => {
    db.all(`SELECT * FROM ejemplares WHERE libro_id = ?`, [req.params.id], (err, rows) => {
        if (err) return res.status(400).json({ error: err.message });
        res.json(rows);
    });
});

// Iniciar el servidor
app.listen(port, () => {
    console.log(`Servidor corriendo en http://localhost:${port}`);
});