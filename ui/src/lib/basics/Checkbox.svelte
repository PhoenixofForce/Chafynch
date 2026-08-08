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
	<input class="checkbox" {disabled} type="checkbox" bind:checked={value} />
	{label}
</label>
