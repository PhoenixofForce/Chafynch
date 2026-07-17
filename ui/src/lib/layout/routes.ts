import type { Pathname } from '$app/types';
import { House, Leaf, Store, Cog, Sprout, Palette } from '@lucide/svelte';
import type { Component } from 'svelte';

export type Breadcrumb = {
	label: string;
	path?: Pathname;
	icon?: Component;
};

export const sidebarLinks: Breadcrumb[] = [
	{
		path: '/',
		icon: House,
		label: 'Home'
	},
	{
		path: '/teas',
		icon: Leaf,
		label: 'Teas'
	},
	{
		path: '/vendors',
		icon: Store,
		label: 'Vendors'
	},
	{
		path: '/cultivars',
		icon: Sprout,
		label: 'Cultivars'
	},
	{
		path: '/tea-types',
		icon: Palette,
		label: 'Tea Types'
	},
	{
		path: '/settings' as Pathname,
		icon: Cog,
		label: 'Settings'
	}
];

export function breadcrumbFor(path: Pathname): Breadcrumb | undefined {
	return sidebarLinks.find((l) => l.path === path);
}
