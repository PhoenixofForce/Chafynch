<script lang="ts">
	import { onMount } from 'svelte';
	import { api } from '$lib/api/client';
	import type { TeaDTO, TeaTypeDto } from '$lib/api/types';
	import Autocomplete from '$lib/components/Autocomplete.svelte';
	import SearchableSelect from '$lib/components/SearchableSelect.svelte';
	import { goto } from '$app/navigation';
	import { resolve } from '$app/paths';

	let form = $state<TeaDTO>({
		name: '',
		cultivar: '',
		teaType: '',
		vendor: '',
		originCountry: '',
		originProvince: '',
		originCity: '',
		price: undefined,
		purchaseDate: '',
		weightGrams: undefined
	});

	let teaTypes = $state<TeaTypeDto[]>([]);
	let countryNames = $state<string[]>([]);
	let submitting = $state(false);
	let error = $state('');

	onMount(() => {
		api.GET('/api/tea-types').then(({ data }) => {
			if (data) teaTypes = data;
		});

		fetch('/countries/index.json').then(async (res) => {
			if (res.ok) {
				const entries: { code: string; name: string }[] = await res.json();
				countryNames = entries.map((e) => e.name);
			}
		});
	});

	async function searchCultivars(q: string): Promise<string[]> {
		const { data } = await api.GET('/api/cultivars', { params: { query: { q } } });
		return data?.map((c) => c.name ?? '') ?? [];
	}

	async function searchVendors(q: string): Promise<string[]> {
		const { data } = await api.GET('/api/vendors', { params: { query: { q } } });
		return data?.map((v) => v.vendor.name ?? '') ?? [];
	}

	async function submit() {
		if (form.originCountry && !countryNames.includes(form.originCountry)) {
			error = 'Bitte ein gueltiges Land auswaehlen.';
			return;
		}
		submitting = true;
		error = '';
		const { error: err } = await api.POST('/api/teas', { body: form });
		submitting = false;
		if (err) {
			error = 'Fehler beim Speichern.';
			return;
		}
		await goto(resolve('/'));
	}
</script>

<div class="mx-auto max-w-2xl p-6">
	<h1 class="text-3xl font-bold text-base-content">Neuen Tee hinzufuegen</h1>

	<form
		class="mt-6 space-y-4"
		onsubmit={(e) => {
			e.preventDefault();
			submit();
		}}
	>
		<div class="form-control">
			<label class="label" for="name">Name*</label>
			<input
				id="name"
				type="text"
				class="input-bordered input w-full"
				bind:value={form.name}
				required
			/>
		</div>

		<div class="form-control">
			<label class="label" for="teaType">Sorte</label>
			<select id="teaType" class="select-bordered select w-full" bind:value={form.teaType}>
				<option value="">-- Waehlen --</option>
				{#each teaTypes as t (t.id)}
					<option value={t.name}>{t.name}</option>
				{/each}
			</select>
		</div>

		<div class="form-control">
			<label class="label" for="cultivar">Kultivar</label>
			<Autocomplete
				id="cultivar"
				placeholder="z.B. Da Bai"
				search={searchCultivars}
				bind:value={form.cultivar!}
			/>
		</div>

		<div class="form-control">
			<label class="label" for="vendor">Haendler</label>
			<Autocomplete
				id="vendor"
				placeholder="z.B. Yunnan Sourcing"
				search={searchVendors}
				bind:value={form.vendor!}
			/>
		</div>

		<fieldset class="fieldset">
			<legend class="fieldset-legend">Herkunft</legend>
			<div class="grid grid-cols-3 gap-2">
				<div class="form-control">
					<label class="label" for="originCountry">Land</label>
					<SearchableSelect
						id="originCountry"
						placeholder="z.B. China"
						options={countryNames}
						bind:value={form.originCountry!}
					/>
				</div>
				<div class="form-control">
					<label class="label" for="originProvince">Provinz</label>
					<input
						id="originProvince"
						type="text"
						class="input-bordered input w-full"
						placeholder="z.B. Fujian"
						bind:value={form.originProvince}
					/>
				</div>
				<div class="form-control">
					<label class="label" for="originCity">Stadt</label>
					<input
						id="originCity"
						type="text"
						class="input-bordered input w-full"
						placeholder="z.B. Taimu"
						bind:value={form.originCity}
					/>
				</div>
			</div>
		</fieldset>

		<div class="grid grid-cols-3 gap-4">
			<div class="form-control">
				<label class="label" for="price">Preis</label>
				<input
					id="price"
					type="number"
					step="0.01"
					class="input-bordered input w-full"
					bind:value={form.price}
				/>
			</div>
			<div class="form-control">
				<label class="label" for="weightGrams">Gewicht (g)</label>
				<input
					id="weightGrams"
					type="number"
					step="0.01"
					class="input-bordered input w-full"
					bind:value={form.weightGrams}
				/>
			</div>
			<div class="form-control">
				<label class="label" for="purchaseDate">Kaufdatum</label>
				<input
					id="purchaseDate"
					type="date"
					class="input-bordered input w-full"
					bind:value={form.purchaseDate}
				/>
			</div>
		</div>

		{#if error}
			<div class="alert alert-error">{error}</div>
		{/if}

		<div class="flex gap-2">
			<button type="submit" class="btn btn-primary" disabled={submitting}>
				<span class:hidden={!submitting} class="loading loading-xs loading-ring"></span>
				{submitting ? 'Speichern...' : 'Speichern'}
			</button>
			<a href={resolve('/')} class="btn btn-ghost">Abbrechen</a>
		</div>
	</form>
</div>
