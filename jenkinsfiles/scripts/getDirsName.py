#!/usr/bin/env python
# -*- encoding:utf-8 -*-

import os
import sys

def getDirsName(DirPath):

    return "\n".join(sorted(os.listdir(DirPath),reverse=True)[0:10])


DirPath =  sys.argv[1]

print getDirsName(DirPath)


    