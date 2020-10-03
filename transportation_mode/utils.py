from typing import Tuple

import pandas as pd
from numpy import ndarray


def encode_column(col: ndarray) -> ndarray:
    map_key_val = {
        'Still': 0,
        'Walking': 1,
        'Car': 2,
        'Bus': 3,
        'Train':4
    }

    return col.map(lambda x: map_key_val[x])


def decode_output(num: int) -> str:
    modes = ['Still', 'Walking', 'Car', 'Bus', 'Train']

    return modes[num]


def split_dataset(df: pd.DataFrame) -> Tuple[ndarray, ndarray, ndarray]:
    length = len(df.index) # number of rows
    end_train = int((3 / 4) * length)
    end_test = int((1 / 8) * length) + end_train

    n_df = df.to_numpy()

    return (n_df[:end_train], n_df[end_train:end_test], n_df[end_test:])
