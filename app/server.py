import os
from flask import Flask, send_from_directory, jsonify

app = Flask(__name__)

THIS_FILE_DIR = os.path.dirname(os.path.realpath(__file__))
PROJECT_DIR = os.path.dirname(THIS_FILE_DIR)
RESOURCES_DIR = os.path.join(PROJECT_DIR, 'resources', 'public')

@app.route('/<path:resource>')
def public_resource(resource):
    """Serves a static resource."""
    return send_from_directory(RESOURCES_DIR, resource)

@app.route('/')
def index():
    """Serve the index."""
    return public_resource('index.html')


@app.route('/newtext')
def newtext():
    return jsonify({'text': 'hello from flask to cljs'})


app.run()
