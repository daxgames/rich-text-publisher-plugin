package org.korosoft.jenkins.plugin.rtp;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.korosoft.jenkins.plugin.rtp.parser.HTMLMarkupParser;
import org.jvnet.hudson.test.JenkinsRule;

public class RichTextPublisherConfigurationTest {
    @Rule
    public JenkinsRule jenkins = new JenkinsRule();

    @Test
    public void safeHtmlFormattingIsDisabledByDefault() {
        Assert.assertFalse(configuration().isSafeHtmlFormatting());
        String html = "<b>safe</b><script>alert(1)</script>";

        Assert.assertEquals(html, new HTMLMarkupParser().parse(html));
    }

    @Test
    public void safeHtmlFormattingSanitizesNewAndStoredMessagesWhenEnabled() {
        configuration().setSafeHtmlFormatting(true);
        String html = "<b>safe</b><script>alert(1)</script>";

        String parsed = new HTMLMarkupParser().parse(html);
        String stored = new BuildRichTextAction(null, parsed).getRichText();

        Assert.assertTrue(parsed.contains("<b>safe</b>"));
        Assert.assertFalse(parsed.contains("<script"));
        Assert.assertTrue(stored.contains("<b>safe</b>"));
        Assert.assertFalse(stored.contains("<script"));

        configuration().setSafeHtmlFormatting(false);
        String parsedWithSafeModeDisabled = new HTMLMarkupParser().parse(html);
        Assert.assertEquals(html, parsedWithSafeModeDisabled);
        Assert.assertEquals(parsed, new BuildRichTextAction(null, parsed).getRichText());
        Assert.assertEquals(html, new BuildRichTextAction(null, parsedWithSafeModeDisabled).getRichText());
    }

    @Test
    public void repeatedPublisherCallsKeepEarlierMessagesOnTheBuild() throws Exception {
        configuration().setSafeHtmlFormatting(false);
        hudson.model.Run<?, ?> build = jenkins.createFreeStyleProject().scheduleBuild2(0).get();

        BuildRichTextAction first = new BuildRichTextAction(build, "<b>first</b>");
        build.addAction(first);
        BuildRichTextAction second = new BuildRichTextAction(build, "<i>second</i>");
        build.addOrReplaceAction(second);

        Assert.assertEquals("<b>first</b><i>second</i>", build.getAction(BuildRichTextAction.class).getRichText());
    }

    private RichTextPublisherConfiguration configuration() {
        return jenkins.jenkins.getExtensionList(RichTextPublisherConfiguration.class)
                .get(RichTextPublisherConfiguration.class);
    }
}