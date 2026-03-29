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
export const links: { path: Route; icon: Component; text: string }[] = [
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
