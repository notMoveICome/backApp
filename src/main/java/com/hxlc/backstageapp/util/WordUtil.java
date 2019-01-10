package com.hxlc.backstageapp.util;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * word转换为odf
 *
 * @author xyz
 */
public class WordUtil {

    public static final int WORD = 0;
    public static final int HTML = 8;
    public static final int XML = 11;
    public static final int PDF = 17;
    public static final int ODT = 23;
    private static Logger logger = LoggerFactory.getLogger(WordUtil.class);

    public static String typeExtension(int type) {

        String ext = "doc";
        switch (type) {
            case WORD:
                ext = "doc";
                break;
            case HTML:
                ext = "html";
                break;
            case XML:
                ext = "xml";
                break;
            case PDF:
                ext = "pdf";
                break;
            case ODT:
                ext = "odt";
                break;
            default:
                break;
        }

        return ext;
    }

    static int getFileType(String to) {
        int fileType = WORD;
        String ext = FilenameUtils.getExtension(to);
        switch (ext.toLowerCase()) {
            case "doc":
                fileType = WORD;
                break;
            case "docx":
                fileType = WORD;
                break;
            case "html":
                fileType = HTML;
                break;
            case "xml":
                fileType = XML;
                break;
            case "pdf":
                fileType = PDF;
                break;
            case "odt":
                fileType = ODT;
                break;
            default:
                break;
        }
        return fileType;
    }


    public static void wordSaveAs(String from, String to) {
        ActiveXComponent app = null;
        ComThread.InitSTA(true);
        try {
            app = new ActiveXComponent("Word.Application");

            app.setProperty("Visible", new Variant(false));// 设置word应用程序窗体不可见
            Dispatch docs = app.getProperty("Documents").toDispatch();
            Dispatch doc = Dispatch.invoke(docs, "Open", Dispatch.Method, new Object[]{from, new Variant(false), new Variant(true)},
                    new int[1]).toDispatch();

            Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[]{to, new Variant(getFileType(to))}, new int[1]);
            Dispatch.call(doc, "Close", new Variant(false));
        } catch (Exception e) {
            logger.error("转换出错,详情：" + e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            try {
                if (app != null) {
                    app.invoke("Quit", new Variant[]{});
                }
            } catch (Exception e2) {
                logger.error("关闭错误：" + e2.getLocalizedMessage());
                e2.printStackTrace();
            }
        }
        ComThread.Release();
    }

    /**
     * WORD转ODT jacob-1.18-x64.dll、jacob-1.18-x86.dll拷贝到C:\Windows\System32和C:\Program Files\Java\jdk1.7.0_75\jre\bin
     *
     * @param srcFile WORD文件全路径
     * @param odtFile 转换后dot存放路径
     */
    public static void saveAsOdt(String srcFile, String odtFile) {
        saveAs(srcFile, odtFile, DocFormat.ODT);
    }

    /**
     * @param srcFile word文件全路径
     * @param xmlfile 转换后xml存放路径
     */
    public static void saveAsXml(String srcFile, String xmlfile) {
        saveAs(srcFile, xmlfile, DocFormat.XML);
    }

    public static void saveAsDoc(String srcFile, String docFile) {
        saveAs(srcFile, docFile, DocFormat.DOC);
    }

    public static void saveAsDocx(String srcFile, String docxFile) {
        saveAs(srcFile, docxFile, DocFormat.DOCX);
    }

    public static void saveAsHtml(String srcFile, String htmlFile) {
        saveAs(srcFile, htmlFile, DocFormat.HTML);
    }

    public static void saveAsPdf(String srcFile, String pdfFile) {
        saveAs(srcFile, pdfFile, DocFormat.PDF);
    }

    private static void saveAs(String srcFile, String dstFile, int type) {
        ActiveXComponent app;
        try {
            app = new ActiveXComponent("Word.Application");
        } catch (Exception e) {
            logger.error("dll文件缺失,无法创建应用程序实例");
            throw new RuntimeException(e.getLocalizedMessage());
        }
        logger.info("*****正在转换...*****");
        try {
            // 设置word应用程序不可见
            app.setProperty("Visible", new Variant(false));
            // documents表示word程序的所有文档窗口，（word是多文档应用程序）
            Dispatch docs = app.getProperty("Documents").toDispatch();
            // 打开要转换的word文件
            Dispatch doc = Dispatch.invoke(docs, "Open", Dispatch.Method, new Object[]{srcFile, new Variant(false), new Variant(true)},
                    new int[1]).toDispatch();
            // 作为xml格式保存到临时文件
            Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[]{dstFile, new Variant(type)}, new int[1]);
            // 关闭word文件
            Dispatch.call(doc, "Close", new Variant(false));
        } catch (Exception e) {
            logger.error("转换出错", e);
            throw new RuntimeException(e.getLocalizedMessage(), e);
        } finally {
            // 关闭word应用程序
            try {
                app.invoke("Quit", new Variant[]{});
            } catch (Exception e2) {
                logger.error("关闭错误：" + e2.getLocalizedMessage());
            }
        }
        logger.info("*****转换完毕********");
    }

    private static String genImgXmlBlock(String expression, int idx, int width, int height) {
        //图片序号
        String idxFormatStr = String.format("%06d", idx);
        return "<w:p wsp:rsidR=\"00AC7D54\" wsp:rsidRDefault=\"00BC212A\"><w:r><w:pict><v:shapetype id=\"_x0000_t75\" coordsize=\"21600,21600\" o:spt=\"75\" o:preferrelative=\"t\" path=\"m@4@5l@4@11@9@11@9@5xe\" filled=\"f\" stroked=\"f\"><v:stroke joinstyle=\"miter\"/><v:formulas><v:f eqn=\"if lineDrawn pixelLineWidth 0\"/><v:f eqn=\"sum @0 1 0\"/><v:f eqn=\"sum 0 0 @1\"/><v:f eqn=\"prod @2 1 2\"/><v:f eqn=\"prod @3 21600 pixelWidth\"/><v:f eqn=\"prod @3 21600 pixelHeight\"/><v:f eqn=\"sum @0 0 1\"/><v:f eqn=\"prod @6 1 2\"/><v:f eqn=\"prod @7 21600 pixelWidth\"/><v:f eqn=\"sum @8 21600 0\"/><v:f eqn=\"prod @7 21600 pixelHeight\"/><v:f eqn=\"sum @10 21600 0\"/></v:formulas><v:path o:extrusionok=\"f\" gradientshapeok=\"t\" o:connecttype=\"rect\"/><o:lock v:ext=\"edit\" aspectratio=\"t\"/></v:shapetype><w:binData w:name=\"wordml://02" + idxFormatStr + ".jpg\" xml:space=\"preserve\">" + expression + "</w:binData><v:shape id=\"_x0000_i1027\" type=\"#_x0000_t75\" style=\"width:" + width + "pt;height:" + height + "pt\"><v:imagedata src=\"wordml://02000001.jpg\" o:title=\"timg\"/></v:shape></w:pict></w:r></w:p>";
    }

    private static String genImgXmlBlock(String expression, int idx) {
        return genImgXmlBlock(expression, idx, 400, 300);
    }

    /**
     * xml格式的doc文档进行预处理，将图片部分的模板进行xml格式替换
     *
     * @param xmlStr xml格式文档
     * @return
     */
    public static String preHandleXmlDoc(String xmlStr) {
        int startIndex = xmlStr.indexOf("<wx:sect>") + 9;
        int endIndex = xmlStr.lastIndexOf("</wx:sect>");
        String pBody = xmlStr.substring(startIndex, endIndex);
        int pLen = pBody.length();
        int fromPos = 0;
        int picIndex = 1;
        StringBuffer sb = new StringBuffer();
        while (fromPos <= pLen) {
            int toPos = pBody.indexOf("</w:p>", fromPos) + 6;
            if (toPos < 6) {
                sb.append(pBody.substring(fromPos));
                break;
            } else {
                String p = pBody.substring(fromPos, toPos);
                //暂时只支持每个段落单个图片处理
                int expressionStartPos = p.indexOf("#{img_");
                if (expressionStartPos > 0) {
                    int expressionEndPos = p.lastIndexOf("}");
                    String expression = p.substring(expressionStartPos, expressionEndPos + 1);
                    sb.append(genImgXmlBlock(expression, picIndex++));
                } else {
                    sb.append(p);
                }
                fromPos = toPos;
            }
        }
        return xmlStr.substring(0, startIndex) + sb.toString() + xmlStr.substring(endIndex);
    }

    private class DocFormat {
        public static final int DOC = 0;
        public static final int DOT = 1;
        public static final int RTF = 6;
        public static final int MHT = 9;
        //        public static final int HTML = 8;
        //筛选的HTML
        public static final int HTML = 10;
        public static final int XML = 11;
        public static final int DOCX = 12;
        public static final int PDF = 17;
        public static final int XPS = 18;
        public static final int ODT = 23;
    }

}
