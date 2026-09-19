<script lang="ts">
	import Dropdown from '$lib/basics/Dropdown.svelte';
	import { loadLocale } from 'wuchale/load-utils';
	import { locales } from '../../locales/data';
	import { Cog, User } from '@lucide/svelte';

	const { bottomRight = true }: { bottomRight?: boolean } = $props();

	const saved = localStorage.getItem('theme');
	let isDark = $state(saved === 'forest');
	if (saved) {
		document.documentElement.setAttribute('data-theme', saved);
	}

	function toggleDarkMode(value?: boolean) {
		isDark = value ?? !isDark;
		const theme = isDark ? 'forest' : 'lemonade';
		localStorage.setItem('theme', theme);
		document.documentElement.classList.add('no-transitions');
		document.documentElement.setAttribute('data-theme', theme);
		requestAnimationFrame(() => {
			document.documentElement.classList.remove('no-transitions');
		});
	}

	let selectedLocale = $state(localStorage.getItem('lang') ?? 'en');
	function setLocale(newLocale: string) {
		selectedLocale = newLocale;
		localStorage.setItem('lang', newLocale);
		loadLocale(newLocale);
	}

	//Todo: sort locals ascending
</script>

<div class="{bottomRight ? 'fixed right-4 bottom-4' : ''} z-50 flex flex-row gap-4">
	<Dropdown
		class={bottomRight ? '' : 'rounded-full btn-primary'}
		disableStyle={bottomRight}
		dropdownClass={bottomRight ? 'dropdown-left dropdown-end' : 'dropdown-bottom'}
		icon={bottomRight ? Cog : User}
	>
		<li>
			<Dropdown
				class="menu-dropdown-toggle"
				disableStyle
				dropdownClass="dropdown-left {bottomRight ? 'dropdown-end' : 'dropdown-start'}"
				label="Language ({selectedLocale.toLocaleUpperCase()})"
				width="w-fit"
			>
				{#each locales as locale (locale)}
					<li>
						<button
							class="capitalize {locale === selectedLocale ? 'menu-active' : ''}"
							onclick={() => setLocale(locale)}
						>
							{new Intl.DisplayNames([locale], { type: 'language' }).of(locale)}
						</button>
					</li>
				{/each}
			</Dropdown>
		</li>
		<li>
			<Dropdown
				class="menu-dropdown-toggle"
				disableStyle
				dropdownClass="dropdown-left {bottomRight ? 'dropdown-end' : 'dropdown-start'}"
				label="Theme ({isDark ? 'Dark' : 'Light'})"
				width="w-fit"
			>
				<li>
					<button class:menu-active={!isDark} onclick={() => toggleDarkMode(false)}> Light </button>
				</li>
				<li>
					<button class:menu-active={isDark} onclick={() => toggleDarkMode(true)}> Dark </button>
				</li>
			</Dropdown>
		</li>
	</Dropdown>
</div>
