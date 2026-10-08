# Rich Text Publisher Plugin

Now featuring pipeline plugin support

A plugin adds a configurable post-build step which publishes arbitrary rich text at the Project and Build pages.
The following markup languages are supported: HTML, WikiText, Confluence. Build parameters or whole files from workspace may be embedded into text.

Copyright (c) 2013 Dmitry Korotkov. See LICENSE for further details.

## Safe HTML formatting

In **Manage Jenkins > System**, enable **Use safe HTML formatting** to preserve
common HTML formatting while removing scripts, event handlers, and unsafe links.
The setting is disabled by default for compatibility; with it disabled, messages
use the previous unsanitized HTML behavior.

The setting can also be enabled with Configuration as Code:

```yaml
unclassified:
	richTextPublisher:
		safeHtmlFormatting: true
```
