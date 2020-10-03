from json import loads

from flask import Flask
from numpy import array
from tensorflow import keras

from utils import decode_output

app = Flask(__name__)
model = keras.models.load_model('model')


@app.route('/', methods=['POST'])
def index():
    test = loads(request.get_json()) # load variables from json request
    out = model.predict(array(test.values())) # pass to array and predict mode

    return decode_output(argmax(out)) # humanize output


if __name__ == '__main__':

    print('Hello')
