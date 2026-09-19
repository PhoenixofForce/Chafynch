<script lang="ts">
	import type { ComponentProps, Snippet } from 'svelte';
	import Button from './Button.svelte';

	const uid = $props.id();
	const {
		width = 'w-52',
		children,
		disableStyle = false, // replace with button variant
		dropdownClass,
		class: className,
		...rest
	}: {
		width?: string;
		children?: Snippet;
		disableStyle?: boolean;
		dropdownClass?: string;
	} & Omit<ComponentProps<typeof Button>, 'noAnimation' | 'variant' | 'popovertarget'> = $props();
</script>

<Button
	class={className}
	noAnimation
	variant={disableStyle ? 'plain' : 'base'}
	{...rest}
	style="anchor-name:--anchor-{uid}; {rest.style}"
	popovertarget="popover-{uid}"
/>
<ul
	id="popover-{uid}"
	style="position-anchor:--anchor-{uid}"
	class="menu dropdown ml-0 {width} rounded-box bg-base-200 shadow-sm before:hidden {dropdownClass} "
	popover
>
	{@render children?.()}
</ul>
