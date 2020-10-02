import numpy as np
from tensorflow import keras


def decode_output(num):
    modes = ['Still', 'Walking', 'Car', 'Bus', 'Train']
    return modes[num]


if __name__ == '__main__':
    model = keras.models.load_model('model')

    test = np.array()

    out = model.predict(test)
    print(f'predicted: {decode_output(argmax(out))}')
 