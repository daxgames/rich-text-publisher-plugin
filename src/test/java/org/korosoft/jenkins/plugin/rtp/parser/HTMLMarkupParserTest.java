package org.korosoft.jenkins.plugin.rtp.parser;

import org.junit.Assert;
import org.junit.Test;
import org.korosoft.jenkins.plugin.rtp.BuildRichTextAction;

public class HTMLMarkupParserTest {
    @Test
    public void removesUnsafeMarkupAndPreservesFormatting() {
        String result = HtmlSanitizer.sanitizeHtml("<p><b>Safe formatting</b></p>"
                + "<table border='1' cellpadding='4' cellspacing='0' style='border-collapse:collapse;border:1px solid #000;"
                + "background-image:url(javascript:alert(1))'><tr><td border='1' style='border:1px solid red'>cell</td></tr></table>"
                + "<script>alert('xss')</script><img src=x onerror=alert('xss')>"
                + "<a href='javascript:alert(1)'>unsafe link</a>");

        Assert.assertTrue(result.contains("<b>Safe formatting</b>"));
        Assert.assertTrue(result.contains("border=\"1\""));
        Assert.assertTrue(result.contains("cellpadding=\"4\""));
        Assert.assertTrue(result.contains("cellspacing=\"0\""));
        Assert.assertTrue(result.contains("border-collapse:collapse"));
        Assert.assertTrue(result.contains("border:1px solid #000"));
        Assert.assertTrue(result.contains("border:1px solid red"));
        Assert.assertTrue(result.contains("<td border=\"1\""));
        Assert.assertTrue(result.contains(">cell</td>"));
        Assert.assertFalse(result.contains("background-image"));
        Assert.assertFalse(result.contains("<script"));
        Assert.assertFalse(result.contains("alert('xss')"));
        Assert.assertFalse(result.contains("onerror"));
        Assert.assertFalse(result.contains("javascript:"));
    }

    @Test
    public void leavesPreviouslyStoredBuildMessagesUnchangedByDefault() {
        BuildRichTextAction action = new BuildRichTextAction(null,
                "<b>Safe formatting</b><script>alert('xss')</script>");

        String result = action.getRichText();

        Assert.assertEquals("<b>Safe formatting</b><script>alert('xss')</script>", result);
    }
}