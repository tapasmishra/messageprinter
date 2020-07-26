package io.jenkins.plugins.messageprinter;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Descriptor;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;
import javax.annotation.Nonnull;

public class printerWrapper extends BuildWrapper {

    public static final int BYTES_IN_MEGABYTE = 1024 * 1024;

    @DataBoundConstructor
    public printerWrapper() {}

    @Override
	public Environment setUp(
      AbstractBuild build,
      Launcher launcher,
      BuildListener listener) {
        return new Environment() {
 
            @Override
            public boolean tearDown(
              AbstractBuild build, BuildListener listener)
              throws IOException, InterruptedException {
                String myMessage = DESCRIPTOR.getMessageText();
                /** listener.getLogger().println("Log size is: " + build.getLogText().length() / BYTES_IN_MEGABYTE); */
                listener.getLogger().println("Stuff is: " + myMessage);
                return true;
            }
        };
    }

    @Override
    public Descriptor<BuildWrapper> getDescriptor() {
        return DESCRIPTOR;
    }

    /**Creates descriptor for the BuildWrapper.*/
    @Extension
    public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

    /**The Descriptor for the BuildWrapper.*/   
    public static final class DescriptorImpl extends BuildWrapperDescriptor {
        
        private String defaultStuff = "junk";

        private String messageText;
        
        private boolean globalEnabled;

        /**Constructor loads previously saved form data.*/
        DescriptorImpl() {
            super(printerWrapper.class);
            load();
        }

        public String getMessageText() {
            return messageText;
        }

        public void setMessageText(String consoleMessage) {
            messageText = consoleMessage;
        }

        public boolean isApplicable(AbstractProject<?, ?> item) {
            return true;
        }

        @Nonnull
        public String getDisplayName() {
            return "Print Console Message";
        }

        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            final String consoleMessage = formData.getString("messageText");
            messageText = consoleMessage != null ? consoleMessage : "Hello World";
            save();
            return super.configure(req, formData);
        }
    }
}