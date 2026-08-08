// Todo: should come from the backend

export interface Infusion {
	startTime: Date;
	infusionTime?: number;
	temperature?: number;
	rating?: number;
	tastingNotes: Record<string, string[]>;
	isRinse?: boolean;
}

export interface Session {
	weight?: number;
	volume?: number;
	brewingMethod?: string;
	location?: string;
	people?: string;
	tastingNotes: Record<string, string[]>; // dry leaf eyes and nose

	infusions: Infusion[];

	rating?: number;
	sessionSummary?: string;
	nextSessionHint?: string;
	startTime?: Date;
}

export type TastingNoteCategory = {
	name: string;
	subCategories: string[];
};

export const categories: TastingNoteCategory[] = [
	{ name: 'Eye', subCategories: ['Wet Leaf', 'Liquor'] },
	{ name: 'Nose', subCategories: ['Wet Leaf', 'Liquor', 'Empty Cup'] },
	{ name: 'Mouth', subCategories: ['Texture', 'Taste', 'Finish'] }
];

export const globalCategories: TastingNoteCategory[] = [
	{ name: 'Eye', subCategories: ['Dry Leaf'] },
	{ name: 'Nose', subCategories: ['Dry Leaf'] }
];

export type Tabs =
	| {
			tab: 'infusion';
			index: number;
	  }
	| {
			tab: 'start';
	  }
	| {
			tab: 'end';
	  };
