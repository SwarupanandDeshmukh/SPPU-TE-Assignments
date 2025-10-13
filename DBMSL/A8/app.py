from flask import Flask, render_template, request, redirect, url_for
import mysql.connector

app = Flask(__name__)

# MySQL connection
db = mysql.connector.connect(
    host="10.10.14.233",
    user="te31411",        # change if needed
    password="te31411", # change if needed
    database="te31411_db"
)
cursor = db.cursor(dictionary=True)

# Home - Read all users
@app.route('/')
def index():
    cursor.execute("SELECT * FROM users")
    users = cursor.fetchall()
    return render_template('index.html', users=users)

# Create user
@app.route('/add', methods=['POST'])
def add():
    name = request.form['name']
    email = request.form['email']
    cursor.execute("INSERT INTO users (name, email) VALUES (%s, %s)", (name, email))
    db.commit()
    return redirect(url_for('index'))

# Update user
@app.route('/update/<int:id>', methods=['POST'])
def update(id):
    name = request.form['name']
    email = request.form['email']
    cursor.execute("UPDATE users SET name=%s, email=%s WHERE id=%s", (name, email, id))
    db.commit()
    return redirect(url_for('index'))

# Delete user
@app.route('/delete/<int:id>')
def delete(id):
    cursor.execute("DELETE FROM users WHERE id=%s", (id,))
    db.commit()
    return redirect(url_for('index'))

if __name__ == "__main__":
    app.run(debug=True)
