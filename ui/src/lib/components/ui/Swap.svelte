<script lang="ts">
	import type { Snippet } from 'svelte';
	import type { ChangeEventHandler, HTMLLabelAttributes } from 'svelte/elements';

	let {
		value = $bindable(false),
		onchange,
		on,
		off,
		class: className,
		...rest
	}: {
		value?: boolean;
		onchange?: ChangeEventHandler<HTMLInputElement>;
		on: Snippet;
		off: Snippet;
	} & HTMLLabelAttributes = $props();
</script>

<label
	class="swap swap-rotate opacity-50 transition-all duration-300 will-change-transform hover:scale-110 hover:opacity-100 active:scale-90 {className}"
	{...rest}
>
	<input type="checkbox" bind:checked={value} {onchange} />
	<div class="swap-off">
		{@render off()}
	</div>
	<div class="swap-on">
		{@render on()}
	</div>
</label>
