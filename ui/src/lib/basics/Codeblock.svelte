<script lang="ts">
	import { Clipboard } from '@lucide/svelte';
	import type { HTMLAttributes } from 'svelte/elements';

	let {
		text,
		class: className,
		...rest
	}: { text?: string } & HTMLAttributes<HTMLDivElement> = $props();

	let tooltipText = $state('Copy');
	function onClick() {
		if (!text) return;
		tooltipText = 'Copied!';
		navigator.clipboard.writeText(text);
	}

	// Todo: use for error.svelte
</script>

<div class="mockup-code relative h-86 w-full overflow-y-scroll {className}" {...rest}>
	{#each text?.split('\n') as line, i (i)}
		<pre class="" data-prefix={i + 1}>
			<code>{line}</code>
		</pre>
	{/each}
	<div class="tooltip absolute tooltip-left top-3 right-6 tooltip-accent" data-tip={tooltipText}>
		<button class="btn" onclick={onClick}>
			<Clipboard />
		</button>
	</div>
</div>
