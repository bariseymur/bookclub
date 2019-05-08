"""
#from bookclub.bookclub_server.wishlistcontroller import drag
import bookclub_server.wishlistcontroller as wlc
import pytest
import requests
from django.http import HttpRequest

from bookclub import settings as base_settings
from django.conf import settings as django_settings
import numpy as np
import pandas as pd
import xarray as xr
from unittest.mock import patch
from unittest import mock
from unittest.mock import MagicMock
"""
import pytest
from pytest import fail


def test_user_in_session_drag():
    assert (2+1) == 4
def test_input_json_is_correct_drag():
    assert (2+1) == 4
def test_if_wishlist_entry_exists():
    assert (2+1) == 4
def test_if_wishlist_entry_belongs_to_user():
    assert (2+1) == 4
def test_if_wishlist_table_updated():
    assert (2+1) == 4


