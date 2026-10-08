package org.korosoft.jenkins.plugin.rtp;

import hudson.Extension;
import hudson.ExtensionList;
import jenkins.model.GlobalConfiguration;
import jenkins.model.Jenkins;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundSetter;

@Extension
@Symbol("richTextPublisher")
public class RichTextPublisherConfiguration extends GlobalConfiguration {
    private boolean safeHtmlFormatting;

    public RichTextPublisherConfiguration() {
        load();
    }

    @Override
    public String getDisplayName() {
        return "Rich Text Publisher";
    }

    public boolean isSafeHtmlFormatting() {
        return safeHtmlFormatting;
    }

    @DataBoundSetter
    public void setSafeHtmlFormatting(boolean safeHtmlFormatting) {
        this.safeHtmlFormatting = safeHtmlFormatting;
        save();
    }

    public static boolean isSafeHtmlFormattingEnabled() {
        Jenkins jenkins = Jenkins.getInstanceOrNull();
        if (jenkins == null) {
            return false;
        }
        ExtensionList<RichTextPublisherConfiguration> configurations =
                jenkins.getExtensionList(RichTextPublisherConfiguration.class);
        RichTextPublisherConfiguration configuration = configurations.get(RichTextPublisherConfiguration.class);
        return configuration != null && configuration.isSafeHtmlFormatting();
    }
}