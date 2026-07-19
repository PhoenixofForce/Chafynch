<script lang="ts">
	import { onMount } from 'svelte';
	import { api } from '$lib/api/client';
	import type { TeaDTO, TeaTypeDto } from '$lib/api/types';
	import { goto, invalidateAll } from '$app/navigation';
	import { resolve } from '$app/paths';
	import Select from '$lib/basics/Select.svelte';
	import Combobox from '$lib/basics/Combobox.svelte';
	import { icons } from '$lib/basics/icons';
	import Input from '$lib/basics/Input.svelte';
	import Markdown from '$lib/basics/Markdown.svelte';
	import Textarea from '$lib/basics/Textarea.svelte';
	import Loading from '$lib/basics/Loading.svelte';

	let {
		form = $bindable({
			name: '',
			cultivar: '',
			teaType: '',
			vendor: '',
			harvestYear: undefined,
			harvestLabel: undefined,
			descriptionMd: '',
			originCountry: '',
			originProvince: '',
			originCity: '',
			price: undefined,
			purchaseDate: '',
			weightGrams: undefined
		})
	}: { form: TeaDTO } = $props();

	let editing = $derived<boolean>(form.id !== undefined);

	let teaTypes = $state<TeaTypeDto[]>([]);
	let countryNames = $state<string[]>([]);
	let submitting = $state(false);
	let error = $state('');

	onMount(() => {
		api.GET('/api/tea-types').then(({ data }) => {
			if (data) teaTypes = data;
		});

		// Todo: turn into simple import instead of fetch
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
			error = 'Choose a valid country.';
			return;
		}
		submitting = true;
		error = '';

		let id = form.id;
		const { data, error: err } = await save();
		id = data?.id;

		submitting = false;
		if (err) {
			error = 'Error saving tea.';
			return;
		}

		await invalidateAll();
		await goto(backlink(id));
	}

	function backlink(id: number | undefined) {
		if (id) {
			return resolve('/teas/[slug]', { slug: id + '' });
		}
		return resolve('/teas');
	}

	function save() {
		if (editing) {
			return api.PUT('/api/teas/{id}', {
				body: form,
				params: { path: { id: form.id! } }
			});
		}
		return api.POST('/api/teas', { body: form });
	}
</script>

<h1 class="text-3xl font-bold text-base-content">
	{editing ? 'Updating a tea' : 'Adding a new tea'}
</h1>

<form
	class="mt-6 space-y-4"
	onsubmit={(e) => {
		e.preventDefault();
		submit();
	}}
>
	<div class="form-control">
		<Input
			placeholder="Name*"
			inputClass="w-full"
			bind:value={form.name}
			required
			hint="Name is required"
		/>
	</div>

	<div class="form-control">
		<Select
			label="Tea Type"
			prompt="Choose a tea type"
			bind:value={form.teaType}
			options={teaTypes.map((t) => ({ value: t.name, label: t.name }))}
		/>
	</div>

	<div class="form-control">
		<Combobox
			options={[]}
			placeholder="Cultivar z.B. Da Bai"
			search={searchCultivars}
			bind:value={form.cultivar!}
			icon={icons.cultivar}
		/>
	</div>

	<div class="form-control">
		<Combobox
			options={[]}
			placeholder="Vendor"
			search={searchVendors}
			bind:value={form.vendor!}
			icon={icons.vendor}
		/>
	</div>

	<div class="form-control">
		<div class="grid grid-cols-2 gap-4">
			<Input
				type="number"
				placeholder="Harvest Year"
				step="1"
				inputClass="w-full"
				min={0}
				max={new Date().getFullYear()}
				hint="Cant be in the future"
				bind:value={form.harvestYear}
			/>

			<Input placeholder="Harvest Label" inputClass="w-full" bind:value={form.harvestLabel} />
		</div>
	</div>

	<div class="form-control">
		<div class="grid grid-cols-2 gap-4">
			<Textarea placeholder="Description" bind:value={form.descriptionMd} />

			<Markdown md={form.descriptionMd}>
				{#snippet before()}
					<h3>Preview</h3>
				{/snippet}
			</Markdown>
		</div>
	</div>

	<fieldset class="fieldset">
		<legend class="fieldset-legend flex items-start">Origin</legend>
		<div class="grid grid-cols-3 gap-4">
			<div class="form-control">
				<Combobox options={countryNames} placeholder="Country" bind:value={form.originCountry!} />
			</div>
			<div class="form-control">
				<Input inputClass=" w-full" placeholder="Province" bind:value={form.originProvince} />
			</div>
			<div class="form-control">
				<Input inputClass="w-full" placeholder="City" bind:value={form.originCity} />
			</div>
		</div>
	</fieldset>

	<fieldset class="fieldset">
		<legend class="fieldset-legend flex items-start">Purchase</legend>
		<div class="grid grid-cols-3 gap-4">
			<div class="form-control">
				<Input
					type="number"
					placeholder="Price"
					step="0.01"
					inputClass="w-full"
					min={0}
					hint="Must be at least 0"
					bind:value={form.price}
				/>
			</div>
			<div class="form-control">
				<Input
					type="number"
					placeholder="Weight"
					step="0.01"
					inputClass="w-full"
					min={0}
					hint="Must be at least 0"
					bind:value={form.weightGrams}
				/>
			</div>
			<div class="form-control">
				<Input
					type="date"
					placeholder="Date of Purchase"
					inputClass="w-full"
					bind:value={form.purchaseDate}
				/>
			</div>
		</div>
	</fieldset>

	{#if error}
		<div class="alert alert-error">{error}</div>
	{/if}

	<div class="flex gap-2">
		<button type="submit" class="btn btn-primary" disabled={submitting}>
			<Loading class={!submitting ? 'hidden' : ''} />
			{submitting ? 'Saving...' : editing ? 'Update' : 'Create'}
		</button>
		<a href={backlink(form.id)} class="btn btn-ghost">Cancel</a>
	</div>
</form>
