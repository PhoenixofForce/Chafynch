<script lang="ts">
	import DOMPurify from 'dompurify';
	import { marked } from 'marked';
	import type { Snippet } from 'svelte';
	import type { HTMLAttributes } from 'svelte/elements';

	const {
		md,
		before,
		after,
		class: className,
		...rest
	}: { md: string; before?: Snippet; after?: Snippet } & Omit<
		HTMLAttributes<HTMLDivElement>,
		'children'
	> = $props();
	const sanitizedHtml = $derived(
		DOMPurify.sanitize(
			// eslint-disable-next-line no-misleading-character-class
			marked.parse(md.replace(/^[\u200B\u200C\u200D\u200E\u200F\uFEFF]/, ''), { async: false })
		)
	);
</script>

<div class="prose {className}" {...rest}>
	{@render before?.()}
	<!-- eslint-disable-next-line svelte/no-at-html-tags -->
	{@html sanitizedHtml}
	{@render after?.()}
</div>
