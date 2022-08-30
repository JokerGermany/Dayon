#!/bin/sh
ABS_PATH=$(dirname "$(realpath "$0")")
if [ ! -f "${ABS_PATH}/dayon.sh" ]; then
  ABS_PATH=${ABS_PATH}/dayon/dayon.sh
else
  ABS_PATH=${ABS_PATH}/dayon.sh
fi
"${ABS_PATH}" mpo.dayon.assisted.AssistedRunner "$@"