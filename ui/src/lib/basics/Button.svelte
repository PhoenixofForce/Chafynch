<script lang="ts">
	import type { Component, Snippet } from 'svelte';
	import type { HTMLButtonAttributes } from 'svelte/elements';
	import Loading from './Loading.svelte';

	const variants = {
		base: 'btn',
		plain: ''
	};

	let {
		variant = 'base',
		label,
		children,
		loading,
		icon: Icon,
		class: className,
		disabled,
		noAnimation,
		...rest
	}: {
		variant?: keyof typeof variants;
		label?: string;
		children?: Snippet;
		loading?: boolean;
		icon?: Component;
		noAnimation?: boolean;
	} & HTMLButtonAttributes = $props();
</script>

<button
	class="{variants[variant]} {className} transition-transform {noAnimation
		? ''
		: 'hover:-translate-y-1'} hover:shadow"
	class:btn-square={!label}
	disabled={disabled || loading}
	type="button"
	{...rest}
>
	{#if !loading}
		<Icon />
	{:else}
		<Loading class="loading-sm" />
	{/if}
	{label}
	{@render children?.()}
</button>
