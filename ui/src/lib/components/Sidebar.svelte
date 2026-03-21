<script lang="ts">
	import { Cog, House, Leaf, Store } from '@lucide/svelte';
	import { resolve } from '$app/paths';
	import type { Component } from 'svelte';
	import { page } from '$app/state';

	type Route = Parameters<typeof resolve>[0];
	const links: { path: Route; icon: Component; text: string }[] = [
		{
			path: '/',
			icon: House,
			text: 'Home'
		},
		{
			path: '/teas',
			icon: Leaf,
			text: 'Teas'
		},
		{
			path: '/vendors',
			icon: Store,
			text: 'Vendors'
		},
		{
			path: '/settings',
			icon: Cog,
			text: 'Settings'
		}
	];
	const currentPath = $derived(page.url.pathname);
</script>

<ul class="menu mt-2 w-full">
	{#each links as link (link.path)}
		<li class="mb-2">
			<a
				href={resolve(link.path)}
				class="{currentPath === link.path
					? 'bg-neutral text-neutral-content'
					: 'opacity-70 transition-opacity hover:opacity-100'} is-drawer-close:tooltip is-drawer-close:tooltip-right"
				data-tip={link.text}
			>
				<link.icon size="21" />
				<span class="is-drawer-close:hidden">{link.text}</span>
			</a>
		</li>
	{/each}
</ul>
