import type { Pathname } from '$app/types';
import { icons } from '$lib/basics/icons';
import { House, Cog } from '@lucide/svelte';
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
		icon: icons.tea,
		label: 'Teas'
	},
	{
		path: '/vendors',
		icon: icons.vendor,
		label: 'Vendors'
	},
	{
		path: '/cultivars',
		icon: icons.cultivar,
		label: 'Cultivars'
	},
	{
		path: '/tea-types',
		icon: icons.teaType,
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
