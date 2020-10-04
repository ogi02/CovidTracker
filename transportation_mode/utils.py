from typing import Tuple

import pandas as pd
from numpy import array, ndarray


def encode_column(col: ndarray) -> ndarray:
    ''' Encode column

    encodes a label column into integers for training

    Parameters:
    -----------
     - col : ndarray, column from a pandas dataframe

    Returns:
    -----------
     - column : ndarray

    '''

    map_key_val = {'Still': 0, 'Walking': 1, 'Car': 2, 'Bus': 3, 'Train': 4}

    return col.map(lambda x: map_key_val[x])


def decode_output(num: int) -> str:
    ''' Decode output

    decode integer into string after training for testing

    Parameters:
    -----------
     - num : int, encoded output from model

    Returns:
    -----------
     - type : str

    '''
    modes = ['still', 'on a walk', 'in a car', 'on a bus', 'on a train']

    return modes[num]


def split_dataset(df: pd.DataFrame) -> Tuple[ndarray, ndarray, ndarray]:
    ''' Split dataset

    splits dataset into 3 parts: 75% train, 12.5% test and validate each 

    Parameters:
    -----------
     - df : pandas DataFrame, dataframe to split

    Returns:
    -----------
     - sets : tuple(ndarray, ndarray, ndarray)

    '''
    length = len(df.index)  # number of rows
    end_train = int((3 / 4) * length)
    end_test = int((1 / 8) * length) + end_train

    n_df = df.to_numpy()

    return (n_df[:end_train], n_df[end_train:end_test], n_df[end_test:])


def parse_json(json: dict) -> dict:
    ''' Parse JSON

    un-nest JSON from request to be used for predicting

    Parameters:
    -----------
     - json : dict, JSON object from request

    Returns:
    -----------
     - parameters(unordered) : dict

    '''
    parsed = {}

    for key in json:
        for value in json[key]:
            param_name = key + '#' + value
            parsed[param_name] = json[key][value]

    return parsed


def order_params(params: dict) -> ndarray:
    ''' Order parameters

    get parmaters and order them to the exact liking of the netork

    Parameters:
    -----------
     - params : dict, unordered parameters

    Returns:
    -----------
     - parameters : ndarray

    '''
    order_params = [
        'accelerometer', 'game_rotation_vector', 'gyroscope',
        'gyroscope_uncalibrated', 'linear_acceleration', 'orientation',
        'rotation_vector', 'sound'
    ]  # missing orientation and sound

    order_types = ['mean', 'min', 'max', 'std']

    parsed_params = []
    ''' template for params is PARAM_NAME#TYPE'''
    for param in order_params:
        for value in order_types:
            parsed_params.append(params[param + '#' + value])

    return array(parsed_params)
