<script lang="ts">
	import type { Component, Snippet } from 'svelte';
	import type { HTMLButtonAttributes } from 'svelte/elements';
	import Loading from './Loading.svelte';

	let {
		label,
		children,
		loading,
		icon: Icon,
		class: className,
		disabled,
		noAnimation,
		...rest
	}: {
		label?: string;
		children?: Snippet;
		loading?: boolean;
		icon?: Component;
		noAnimation?: boolean;
	} & HTMLButtonAttributes = $props();
</script>

<button
	type="button"
	class="btn {className} transition-transform {noAnimation
		? ''
		: 'hover:-translate-y-1'} hover:shadow"
	class:btn-square={!label}
	disabled={disabled || loading}
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
