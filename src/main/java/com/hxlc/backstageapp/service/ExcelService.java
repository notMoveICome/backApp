package com.hxlc.backstageapp.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface ExcelService {

    void load(String role, String data, HttpServletResponse response) throws IOException;
}
