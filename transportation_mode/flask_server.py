from flask import Flask, jsonify, request
from numpy import argmax
from tensorflow.keras.models import load_model

from utils import decode_output, order_params, parse_json

app = Flask(__name__)
model = load_model('model')


@app.route('/', methods=['POST'])
def index():
    params = order_params(parse_json(
        request.get_json()))  # split and order parameters

    params = params.reshape(1, -1)
    out = model.predict(params)  # pass to array and predict mode

    # humanize output and pass to json
    return jsonify(transport=decode_output(argmax(out)))
