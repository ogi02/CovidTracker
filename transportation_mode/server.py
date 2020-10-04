from flask import Flask, jsonify, request
from numpy import argmax, bincount
from xgboost import XGBClassifier

from utils import decode_output, order_params, parse_json

app = Flask(__name__)
xgb = XGBClassifier().load_model('xgb.dat')


@app.route('/', methods=['GET'])
def greeting():
    return 'Hello, try sending me parameters to predict mode of transportation'


@app.route('/', methods=['POST'])
def index():
    params = order_params(parse_json(
        request.get_json()))  # split and order parameters

    params = params.reshape(1, -1)
    prediction = xgb.predict(params)

    # humanize output and pass to json
    return jsonify(transport=decode_output(prediction))
