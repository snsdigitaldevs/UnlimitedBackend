#!/usr/bin/env python
# -*- encoding:utf-8 -*-

import os
import sys

def getPkgsName(DirPath):

    jarslist = os.listdir(DirPath)
    jarslist.sort(key=lambda x: int(os.path.splitext(x)[0].split("_")[1]),reverse=True)
    return "\n".join(jarslist)

DirPath =  sys.argv[1]

print getPkgsName(DirPath)


    