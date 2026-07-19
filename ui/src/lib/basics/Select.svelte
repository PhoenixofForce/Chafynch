<script lang="ts" generics="ID">
	import type { HTMLSelectAttributes } from 'svelte/elements';

	interface OptionType {
		value: ID;
		label: string;
		disabled?: boolean;
	}

	let {
		label,
		prompt,
		options,
		value = $bindable(null),
		class: className,
		selectClass,
		...rest
	}: {
		label?: string;
		prompt?: string;
		value: ID | '';
		options: OptionType[];
		selectClass?: string;
	} & HTMLSelectAttributes = $props();
</script>

<div>
	<label class="floating-label min-w-fit flex-col {className}">
		<span>{label ?? prompt ?? 'Choose an option'}</span>

		<div class="validator">
			<select
				class="select w-full {selectClass}"
				bind:value
				{...rest}
				{@attach (node) =>
					node.setCustomValidity(
						value === '' || options.find((e) => e.value === value)
							? ''
							: 'Please choose a valid object'
					)}
			>
				<option value="" disabled>{prompt ?? 'Choose an option'}</option>
				{#each options as t (t.value)}
					<option value={t.value} disabled={t.disabled}>{t.label}</option>
				{/each}
			</select>
		</div>
		<p class="validator-hint mt-0 mb-0">Please choose a valid object</p>
	</label>
</div>
