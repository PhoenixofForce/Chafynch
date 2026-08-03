<script lang="ts">
	import type { HTMLLabelAttributes } from 'svelte/elements';

	let {
		value = $bindable(),
		label,
		disabled,
		class: className,
		...rest
	}: { value?: boolean; disabled?: boolean; label: string } & HTMLLabelAttributes = $props();
</script>

<label class="label {className}" {...rest}>
	<input type="checkbox" bind:checked={value} {disabled} class="checkbox" />
	{label}
</label>
