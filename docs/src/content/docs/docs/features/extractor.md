---
title: Vendor Extraction
description: How to import teas directly from the vendors website
---

Instead of adding teas manually, you can automatically fetch the provided metadata from vendors websites.

## Creating an extraction profile

An extraction profile is a piece of config that tells the app how to extract the teas metadata. The app uses [jsoup](https://jsoup.org) for that.

Head to the settings page and select the extraction profile tab. There you can manage all the profiles in your instance. When editing a profile you have some global settings and settings per metadata field. The metadata you can fetch includes title, description, origin, harvest and cultivar.

### Settings Overview

| Setting    | Type         | What it does                                                                                      |
| ---------- | ------------ | ------------------------------------------------------------------------------------------------- |
| Name       | Global       | Name for the profile. This will also be used as the name for the vendor when extracting.          |
| Valid Urls | Global       | Tells the extractor what urls and paths this profile is valid for                                 |
| Selector   | Per Metadata | jsoup css selector to find the node to operate on                                                 |
| Operations | Per Metadata | jsoup operations on how to traverse further from the node                                         |
| Regex      | Per Metadata | Once on the final node, extract only certain parts from the text contained                        |
| Grab All   | Per Metadata | Whether to run the pipeline for all the valid nodes matching the _Selector_ or just the first one |

<!-- Todo: explain settings in detail -->

## Common Patterns

### Simple String

```html
<div class="title">Here is the title</div>
```

Here we just need the Selector `.title`. Everything else can stay empty.

### Multiple Paragraph Description

```html
<div class="description">
	<p>First Paragraph</p>
	<p>Second Paragraph</p>
	<p>Third Paragraph</p>
</div>
```

If we would use the selector `.description` we would get one line containing everything. Like this

```
First Paragraph Second Paragraph Third Paragraph
```

It is recommended to target the inner p-Element with `.description p` and activate grabAll, which results in

```
First Paragraph

Second Paragraph

Third Paragraph
```

### Inline Listing

```html
<div class="field">
	<strong>Origin:</strong>
	China
</div>
```

Here we target the strong-Element with `strong:contains(Origin)` and add `nextSibling` as an operation, as it jumps to the next available sibling.

```html
<div class="field">
	<strong>Origin:</strong>
	<span>China</span>
</div>
```

Here we would need to use the `nextElementSibling` as it skips text and jumps to the next element. `nextSibling` would target the whitespace between strong and span first, so you would need to add it twice to achieve the same result.
