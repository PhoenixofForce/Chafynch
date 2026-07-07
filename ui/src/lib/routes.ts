import { House, Leaf, Store, Cog } from '@lucide/svelte';
import type { Component } from 'svelte';
import type { RouteIdWithSearchOrHash } from '$app/types';
import type { PathnameWithSearchOrHash } from '$app/types';

export type Breadcrumb = {
	label: string;
	path?: Route;
	icon?: Component;
};

export type Route = RouteIdWithSearchOrHash | PathnameWithSearchOrHash;
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
		path: '/settings' as Route,
		icon: Cog,
		label: 'Settings'
	}
];

export function breadcrumbFor(path: Route): Breadcrumb | undefined {
	return sidebarLinks.find((l) => l.path === path);
}
